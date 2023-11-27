package com.example.mypetproject2.features.ui.games.choose

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
import com.example.mypetproject2.data.listTriple
import com.example.mypetproject2.data.separateList
import com.example.mypetproject2.databinding.FragmentChooseWordBinding
import com.example.mypetproject2.features.ui.games.spelling.calculatePercentage
import com.example.mypetproject2.features.ui.games.spelling.getUserAnswers
import com.example.mypetproject2.features.ui.games.spelling.setupOnBackPressedCallback
import com.example.mypetproject2.features.ui.games.stress.StressFragment
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.utils.navigateChooseWordFragmentToFinishedFragment


fun main() {
    var wordIndex = 0
   val list = separateList.shuffled()[wordIndex]
    print(list.first)
}

class ChooseWordFragment : Fragment() {
    private var _binding: FragmentChooseWordBinding? = null
    private val binding get() = _binding!!
    private var currentIndex = 0
    private var correctAnswerIndex: Int = 0

    private var isNextButtonEnabled = true

    private lateinit var viewModel: GamesViewModel

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChooseWordBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayWord()
        initializeButtons()
        setupOnBackPressedCallback()
    }

    private fun initializeButtons() {
        with(binding) {
            b1.setOnClickListener { handleClick(0) }
            b2.setOnClickListener { handleClick(1) }
            b3.setOnClickListener { handleClick(2) }
        }
    }

    private fun buttons() = listOf(binding.b1, binding.b2, binding.b3)

    private fun displayWord() {
        val shuffledTriple = listTriple.shuffled()[currentIndex]
        val options = shuffledTriple.toList().shuffled()
        with(binding) {
            b1.text = options[0]
            b2.text = options[1]
            b3.text = options[2]
        }
        correctAnswerIndex = options.indexOf(shuffledTriple.first)
    }
    
    private fun checkAnswer(clickedButtonIndex: Int) {
        for ((index, button) in buttons().withIndex()) {
            when (index) {
                correctAnswerIndex -> {
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

    private fun handleClick(buttonIndex: Int) {
        if (!isNextButtonEnabled) return
        isNextButtonEnabled = false

        val isCorrect = buttonIndex == correctAnswerIndex
        val userAnswer = buttons()[buttonIndex].text.toString()
        checkAnswer(buttonIndex)

        viewModel.updateScore(isCorrect)
        viewModel.addUserAnswer(isCorrect)
        viewModel.setUserAnswers(userAnswer)
    }

    private fun showNextWord() {
        currentIndex++
        if (currentIndex >= StressFragment.MAX_ATTEMPTS) {
            showGameResults()
            resetBackgroundState()
        } else {
            displayWord()
            resetBackgroundState()
        }
    }

    private fun resetBackgroundState() {
        val defaultBackground =
            ContextCompat.getDrawable(requireContext(), R.drawable.rectangle_111)
        for (button in buttons()) {
            button.background = defaultBackground
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
        navigateChooseWordFragmentToFinishedFragment(
            viewModel.score.value ?: 0,
            percentage,
            userAnswers,
            userAnswerHistory,
            "chooseWord"
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}