package com.example.mypetproject2.features.ui.games.choosespelling

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.separateList
import com.example.mypetproject2.databinding.FragmentChooseSeparateSpellingWordBinding
import com.example.mypetproject2.features.ui.games.stress.GamesFragment
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.utils.navigateChooseSeparateWordFragmentToFinishedFragment
import com.example.mypetproject2.utils.navigateChooseWordFragmentToFinishedFragment

fun main() {
    val list = separateList.shuffled()[0]
    val options = list.toList()
    print("${list.second}, ${list.first}")
}

class ChooseSeparateSpellingWordFragment : Fragment() {
    private var _binding: FragmentChooseSeparateSpellingWordBinding? = null
    private val binding get() = _binding!!
    private val handler = Handler(Looper.getMainLooper())
    var wordIndex = 0

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
            b3.setOnClickListener { handleClick(1) }
            b2.setOnClickListener { handleClick(0) }
            b1.setOnClickListener { handleClick(2) }
        }
    }

    private fun displayWord() {
        val list = separateList.shuffled()[wordIndex]
        val currentWord = list.first
        binding.word.text = currentWord
        currentAnswerIndex = list.second
        Log.d("displayWord", "${list.second}, ${list.first}")
    }

    private fun handleClick(buttonIndex: Int) {
        if (!isNextButtonEnabled) return
        isNextButtonEnabled = false

        val isCorrect = buttonIndex == currentAnswerIndex
        val userAnswer = buttons()[buttonIndex].text.toString()
        checkAnswer(buttonIndex)

        viewModel.updateScore(isCorrect)
        viewModel.addUserAnswer(isCorrect)
        viewModel.setUserAnswers(userAnswer)
    }

    private fun buttons() = listOf(binding.b2, binding.b3, binding.b1)
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
//    }
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
        if (wordIndex >= GamesFragment.MAX_ATTEMPTS) {
            showGameResults()
            resetBackgroundState()
        } else {
            displayWord()
            resetBackgroundState()
        }
    }
    private fun showGameResults() {
        val percentage = calculatePercentage()
        val userAnswers = getUserAnswers()
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

    private fun getUserAnswers(): BooleanArray {
        val userAnswersList = viewModel.userAnswers.value ?: mutableListOf()
        return userAnswersList.toBooleanArray()
    }

    private fun calculatePercentage(): Float {
        return (viewModel.score.value?.toFloat() ?: 0f) / GamesFragment.MAX_ATTEMPTS * 100
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



