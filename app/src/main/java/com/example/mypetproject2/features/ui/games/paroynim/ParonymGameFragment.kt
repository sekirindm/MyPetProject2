package com.example.mypetproject2.features.ui.games.paroynim

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.chaos.view.PinView
import com.example.mypetproject2.R
import com.example.mypetproject2.data.paronymList
import com.example.mypetproject2.databinding.FragmentParoynimGameBinding
import com.example.mypetproject2.features.ui.games.spelling.calculatePercentage
import com.example.mypetproject2.features.ui.games.spelling.getUserAnswers
import com.example.mypetproject2.features.ui.games.spelling.stringBuilder
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.StressFragment
import com.example.mypetproject2.utils.navigateChooseSeparateWordFragmentToFinishedFragment
import com.example.mypetproject2.utils.navigateParonymGameFragmentToFinishedFragment


class ParonymGameFragment : Fragment() {

    private var _binding: FragmentParoynimGameBinding? = null
    private val binding get() = _binding!!
    private var wordIndex = 0
    private var word = ""
    private var currentWord = ""
    private lateinit var viewModel: GamesViewModel
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var pvParonym: PinView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParoynimGameBinding.inflate(inflater, container, false)

        initializeViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayWord()
        val paronym = binding.pvParonym.text
        buttonListener()

        Log.d("paronym", "$paronym")
    }

    private fun initializeViews() {
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        pvParonym = binding.pvParonym
        pvParonym.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().contains(" ")) {
                    val newText = s.toString().replace(" ", "")
                    pvParonym.setText(newText)
                }
            }
        })
    }

    private fun displayWord() {
        val list = paronymList.shuffled()[wordIndex]
        word = list.first
        val formattedWord = stringBuilder(word)
        binding.tvWords.text = formattedWord
        currentWord = list.second
    }

    private fun buttonListener() {
        binding.bNextPage.setOnClickListener {
            val userAnswer = pvParonym.text.toString()
            Log.d("pvParonym", userAnswer)
            Log.d("word", currentWord)
            checkWord(userAnswer)
        }

    }

   private fun checkWord(userAnswer: String) {
        val rightAnswer = userAnswer == currentWord

       viewModel.setUserAnswers(userAnswer)
       viewModel.updateScore(rightAnswer)
       viewModel.addUserAnswer(rightAnswer)

        if (rightAnswer) {
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
       handler.postDelayed({
           resetBackgroundState()
           showNextWord()
       }, 1000L)
    }

    private fun resetBackgroundState() {
        requireView().setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        word = ""
        pvParonym.text = null

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
        navigateParonymGameFragmentToFinishedFragment(
            viewModel.score.value ?: 0,
            percentage,
            userAnswers,
            userAnswerHistory,
            "paronym"
        )
    }


}