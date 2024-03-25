package com.example.mypetproject2.features.ui.games.choosecorrectword.gamenumber13

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentChooseSeparateSpellingWordBinding
import com.example.mypetproject2.utils.navigateChooseSeparateWordFragmentToFinishedFragment
import com.example.mypetproject2.utils.setupOnBackPressedCallback

class ChooseSeparateSpellingWordFragment : Fragment() {
    private var _binding: FragmentChooseSeparateSpellingWordBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModelSeparate: GameSeparateWordViewModel
    private lateinit var tvWord: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGame()
        initObserver()
        setupButtonClickListener()
        setupOnBackPressedCallback()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseSeparateSpellingWordBinding.inflate(inflater, container, false)
        initViewModel()
        return binding.root
    }

    private fun initObserver() {
        viewModelSeparate.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStateSeparate.NewWord -> {
                    tvWord.text = it.word
                    requireView().setBackgroundResource(R.color.white)
                    binding.b1.isEnabled = true
                    binding.b2.isEnabled = true
                }

                is GameStateSeparate.CheckAnswer -> {
                    binding.b1.isEnabled = false
                    binding.b2.isEnabled = false
                    val lastAnswer = it.state.answers.last()
                    val isCorrect = lastAnswer.first == lastAnswer.second
                    Log.d("lastAnswer", "${lastAnswer.first}, ${lastAnswer.second}")

                    val id = if (isCorrect) {
                        R.color.green_light
                    } else {
                        R.color.red_light
                    }
                    requireView().setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(), id
                        )
                    )
                    viewModelSeparate.delay()
                    viewModelSeparate.updateScore(isCorrect)
                }

                is GameStateSeparate.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswers =
                        state.answers.map { pair -> pair.first == pair.second }.toBooleanArray()
                    val userAnswersHistory = state.answers.map { it.second }.toTypedArray()
                    Log.d("userAnswersHistoryChoose", "$userAnswersHistory")
                    navigateChooseSeparateWordFragmentToFinishedFragment(
                        viewModelSeparate.score.value ?: 0,
                        percentage,
                        userAnswers,
                        userAnswersHistory,
                        "chooseSeparateWord"
                    )

                }
            }
        }
    }

    private fun initViewModel() {
        viewModelSeparate = ViewModelProvider(this)[GameSeparateWordViewModel::class.java]
        tvWord = binding.word
    }
    private fun initGame() {
        viewModelSeparate.initGame()
    }

    private fun setupButtonClickListener() {
        binding.b1.setOnClickListener {
            viewModelSeparate.checkAnswer(tvWord.text.toString(), 1)
        }
        binding.b2.setOnClickListener {
            viewModelSeparate.checkAnswer(tvWord.text.toString(), 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}