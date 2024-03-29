package com.example.mypetproject2.features.ui.games.spelling.spellingroot

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.spellingRoot
import com.example.mypetproject2.databinding.FragmentSpellingRootBinding
import com.example.mypetproject2.features.ui.games.spelling.calculatePercentage
import com.example.mypetproject2.features.ui.games.spelling.getUserAnswers
import com.example.mypetproject2.features.ui.games.spelling.setupOnBackPressedCallback
import com.example.mypetproject2.features.ui.games.spelling.transformWord
import com.example.mypetproject2.features.ui.games.stress.StressFragment
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.utils.navigateSpellingRootToGameFinishedFragment
import java.util.Random

class SpellingRootFragment : Fragment() {
    private var _binding: FragmentSpellingRootBinding? = null
    private val binding get() = _binding!!
    private var wordIndex: Int = 0
    private lateinit var viewModel: GamesViewModel
    private lateinit var tvWord: TextView
    private var displayedWord: StringBuilder = StringBuilder()
    private var isLetterRemoved = false
    private var words: String = ""
    private var isUnderscorePresent = false

    private var isNextButtonEnabled = true

    private val random = Random()

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { showNextWord() }


    private val DELAY_MILLIS = 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSpellingRootBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initializeViews()
        generateRandomWord()
        displayWord()

        setTextViewLetters(words)
        setupTextViewClickListeners()
        setupNextPageButtonListener()
        setupOnBackPressedCallback()

        return rootView
    }

    private fun initializeViews() {
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        tvWord = binding.tvSpellingPref
    }

    private fun resetViewState() {
        binding.tvOneN.visibility = View.VISIBLE
        binding.tvTwoN.visibility = View.VISIBLE
        requireView().setBackgroundResource(R.color.white)
    }

    private fun showNextWord() {
        wordIndex++
        if (wordIndex >= StressFragment.MAX_ATTEMPTS) {
            showGameResults()
            resetGame()
        }

        generateRandomWord()
        displayWord()
        resetViewState()

        tvWord.text = displayedWord

        setTextViewLetters(words)

        binding.bNextPage.isEnabled = !isUnderscorePresent

        isNextButtonEnabled = true
    }

    private fun displayWord() {
        val finalWord = displayedWord.toString()
        tvWord.text = finalWord
    }

    private fun generateRandomWord() {
        val spellingRootList = spellingRoot.toList()
        val randomWord = spellingRootList.random().replace("@", "")
        words = randomWord

        displayedWord.clear()
        isUnderscorePresent = false

        var isReplaced = false
        for (i in words.indices) {
            val letter = words[i]

            if (letter.isUpperCase()) {
                if (!isReplaced) {
                    displayedWord.append('_')
                    isReplaced = true
                    isUnderscorePresent = true
                }
            } else {
                displayedWord.append(letter)
            }
        }
    }


    private fun setupTextViewClickListeners() {
        val tvOne = binding.tvOneN
        val tvTwo = binding.tvTwoN

        tvOne.setOnClickListener {
            handleLetterClick(tvOne, tvTwo)
        }

        tvTwo.setOnClickListener {
            handleLetterClick(tvTwo, tvOne)
        }

        tvWord.setOnClickListener {
            handleTvWordClick()
        }
    }

    private fun resetGame() {
        requireView().setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        words = ""
    }

    private fun setTextViewLetters(randomWord: String) {
        val upperCaseLetters = randomWord.filter { it.isUpperCase() }

        val textViews = listOf(binding.tvOneN, binding.tvTwoN)
        val availableTextViews = textViews.toMutableList()

        for (i in upperCaseLetters.indices) {
            val letter = upperCaseLetters[i]

            if (availableTextViews.isNotEmpty()) {
                val randomIndex = (0 until availableTextViews.size).random()
                val textView = availableTextViews[randomIndex]

                textView.text = letter.toString()
                availableTextViews.removeAt(randomIndex)
            } else {
                break
            }
        }
    }

    private fun handleTvWordClick() {
        val word = displayedWord.toString()
        val updatedWord = StringBuilder()

        for (i in word.indices) {
            val letter = word[i]
            if (letter.isUpperCase()) {
                updatedWord.append('_')
            } else {
                updatedWord.append(letter)
            }
        }

        tvWord.text = updatedWord.toString()

        resetViewState()

        isLetterRemoved = false

        binding.bNextPage.isEnabled = !tvWord.text.contains("_")
    }

    private fun handleLetterClick(selectedLetterTextView: TextView, otherLetterTextView: TextView) {
        val selectedLetter = selectedLetterTextView.text.toString()
        val underscoreIndex = tvWord.text.indexOf('_')
        if (underscoreIndex != -1) {
            val updatedWord = tvWord.text.replaceRange(underscoreIndex, underscoreIndex + 1, selectedLetter)
            tvWord.text = updatedWord

            selectedLetterTextView.visibility = View.GONE
            otherLetterTextView.visibility = View.VISIBLE
        }

        binding.bNextPage.isEnabled = !tvWord.text.contains("_")
    }

    private fun setupNextPageButtonListener() {
        binding.bNextPage.setOnClickListener {
            if (isNextButtonEnabled) {
                isNextButtonEnabled = false
                it.isEnabled = false
                val userAnswer = tvWord.text.toString()
                viewModel.getWordCount(userAnswer) // Запросите счетчик
                checkAnswer(userAnswer)

                viewModel.wordCountLiveData.observe(viewLifecycleOwner, androidx.lifecycle.Observer { count ->
                    val isCorrect = userAnswer.equals(transformWord(words), ignoreCase = true)
                    val newCount = if (isCorrect) count + 1 else 0

                    viewModel.insertWordToAllWords(transformWord(words), newCount)
                })
            }
        }
    }


    private fun showGameResults() {
        val percentage = calculatePercentage(viewModel)
        val userAnswers = getUserAnswers(viewModel)
        val userAnswerHistory = viewModel.userAnswersHistory.value?.toTypedArray()!!
        Log.d(
            "showGameResults",
            "userAnswerHistory $userAnswerHistory"
        )
        navigateSpellingRootToGameFinishedFragment(
            viewModel.score.value ?: 0,
            percentage,
            userAnswers,
            userAnswerHistory,
            "spellingroot"
        )
    }

    private fun checkAnswer(userAnswer: String) {
        val transformedWord = transformWord(words)

        val isCorrect = transformedWord == userAnswer
        viewModel.updateScore(isCorrect)
        viewModel.addUserAnswer(isCorrect)
        viewModel.setUserAnswers(userAnswer)

        if (userAnswer.equals(transformedWord, ignoreCase = true)) {
            requireView().setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green_light
                )
            )
        } else {
            requireView().setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red_light
                )
            )
        }
        handler.postDelayed(runnable, DELAY_MILLIS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}