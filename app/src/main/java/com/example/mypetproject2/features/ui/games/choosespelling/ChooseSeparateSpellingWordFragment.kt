package com.example.mypetproject2.features.ui.games.choosespelling

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.separateList
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentChooseSeparateSpellingWordBinding
import com.example.mypetproject2.features.ui.games.spelling.calculatePercentage
import com.example.mypetproject2.features.ui.games.spelling.getUserAnswers
import com.example.mypetproject2.features.ui.games.spelling.stringBuilder
import com.example.mypetproject2.features.ui.games.stress.StressFragment
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.utils.navigateChooseSeparateWordFragmentToFinishedFragment

fun main() {
//    val list = separateList.shuffled()[0]
//    val options = list.toList()
//    print("${list.second}, ${list.first}")

//    val correctDisplay = separateList.find {it.first.replace("(", "").replace(")", "").trim() == rightAnswer.trim()}?.second

//    val kal = separateList.map {
//        val word = it.first
//        when (it.second) {
//            0 -> word.replace("(", "").replace(")", " ")
//            1 -> word.replace("(", "").replace(")", "")
//            2 -> word.replace("(", "").replace(")", "-")
//            else -> word
//        }
//    }

    val w = stress[0].indexOfFirst { it.isUpperCase() }
    print(w)
//
//    kal.forEach { println(it) }

//    val word = "!(НЕ)ВЕРЮ! в то что ты ушел"
//    val startIndex = word.indexOf("!")
//    val endIndex = word.indexOf("!", startIndex + 1)
//    val extractedText = if (startIndex != -1 && endIndex != -1) {
//        word.substring(startIndex, endIndex + 1)
//    } else {
//        "Выделенное слово не найдено"
//    }
//    print(extractedText.replace("!", "").lowercase())
}

class ChooseSeparateSpellingWordFragment : Fragment() {
    private var _binding: FragmentChooseSeparateSpellingWordBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    var wordIndex = 0

    private var currentWord: String = ""

    private var isNextButtonEnabled = true
    private var currentAnswerIndex = 0
    private lateinit var viewModel: GamesViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displayWord()
        initializeButton()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChooseSeparateSpellingWordBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        return binding.root
    }

    private fun initializeButton() {
        with(binding) {
            b1.setOnClickListener { handleClick(1) }
            b2.setOnClickListener { handleClick(0) }
//            b3.setOnClickListener { handleClick(2) }
        }
    }

    private fun displayWord() {
        val list = separateList.shuffled()[wordIndex]
        currentWord = list.first
        val formattedWord = stringBuilder(currentWord)
        binding.word.text = formattedWord
        currentAnswerIndex = list.second
        Log.d("displayWord", "${list.first} ${list.second}, ")
    }

    private fun handleClick(buttonIndex: Int) {
        if (!isNextButtonEnabled) return
        isNextButtonEnabled = false

        val isCorrect = buttonIndex == currentAnswerIndex
//        val userAnswer = buttons()[buttonIndex].text.toString()
        checkAnswer(buttonIndex)

        val userAnswer = when (buttonIndex) {
            0 ->  currentWord.replace("(", "").replace(")", " ")
            1 ->  currentWord.replace("(", "").replace(")", "")
            else -> currentWord.replace("(", "").replace(")", "-")

        }

//        if (buttonIndex == 0) {
//            currentWord.replace("()", " ")
//        }

        viewModel.updateScore(isCorrect)
        viewModel.addUserAnswer(isCorrect)
        viewModel.setUserAnswers(userAnswer)
    }

//    , binding.b3
    private fun buttons() = listOf(binding.b2, binding.b1)
    private fun checkAnswer(clickedButtonIndex: Int) {

        for ((index, button) in buttons().withIndex()) {
            when (index) {
                currentAnswerIndex -> {
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.green_light
                        )
                    )
                }

                clickedButtonIndex -> {
                    button.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.red_light
                        )
                    )
                }
            }
        }
        handler.postDelayed({
            resetBackgroundState()
            isNextButtonEnabled = true
            showNextWord()

        }, 1000L)
    }

    private fun resetBackgroundState() {
        val defaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_111)
        for (button in buttons()) {
            button.background = defaultBackground
        }
    }

    private fun showNextWord() {
        wordIndex++
        if (wordIndex >= StressFragment.MAX_ATTEMPTS) {
            showGameResults()
            resetBackgroundState()
        } else {
            displayWord()
            resetBackgroundState()
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
        navigateChooseSeparateWordFragmentToFinishedFragment(
            viewModel.score.value ?: 0,
            percentage,
            userAnswers,
            userAnswerHistory,
            "chooseSeparateWord"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
