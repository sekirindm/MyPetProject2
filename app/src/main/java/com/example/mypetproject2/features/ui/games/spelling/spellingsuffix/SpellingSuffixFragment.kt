package com.example.mypetproject2.features.ui.games.spelling.spellingsuffix

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentSpellingSuffixBinding
import com.example.mypetproject2.utils.setupOnBackPressedCallback
import com.example.mypetproject2.utils.navigateSpellingSuffixToGameFinishedFragment


class SpellingSuffixFragment : Fragment() {
    private var _binding: FragmentSpellingSuffixBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvWord: TextView
    private lateinit var gameSuffixViewModel: GameSuffixViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSpellingSuffixBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initializeViews()
        initGame()
        initObserver()
        setupTextViewClickListeners()
        setupOnBackPressedCallback()

        return rootView
    }

    private fun initializeViews() {
        gameSuffixViewModel = ViewModelProvider(this)[GameSuffixViewModel::class.java]
        tvWord = binding.tvSpellingPref
    }

    private fun initGame() {
        gameSuffixViewModel.initGame()
    }

    private fun initObserver() {
        gameSuffixViewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStateSuffix.NewWord -> {
                    tvWord.text = it.word
                    resetViewState()
                    binding.tvOne.text = it.letters[0]
                    binding.tvTwo.text = it.letters[1]
                    binding.bNextPage.isEnabled = false
                    binding.tvOne.isEnabled = true
                    binding.tvTwo.isEnabled = true
                }

                is GameStateSuffix.UpdateWord -> {
                    tvWord.text = it.word
                    binding.tvOne.isVisible = it.button != 0
                    binding.tvTwo.isVisible = it.button != 1
                    binding.bNextPage.isEnabled = true
                }

                is GameStateSuffix.CheckedAnswer -> {
                    val lastAnswer = it.state.answers.last()
                    val isCorrect = lastAnswer.first == lastAnswer.second

                    binding.bNextPage.isEnabled = false
                    binding.tvOne.isEnabled = false
                    binding.tvTwo.isEnabled = false

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
                    gameSuffixViewModel.delay()
                    gameSuffixViewModel.updateScore(isCorrect)
                }

                is GameStateSuffix.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswer =
                        state.answers.map { pair -> pair.first == pair.second }.toBooleanArray()
                    val userAnswerHistory = state.answers.map { pair -> pair.second }.toTypedArray()
                    navigateSpellingSuffixToGameFinishedFragment(
                        gameSuffixViewModel.score.value ?: 0,
                        percentage,
                        userAnswer,
                        userAnswerHistory,
                        "spellingsuffix"
                    )

                }
            }
        }
    }

    private fun resetViewState() {
        binding.tvOne.visibility = View.VISIBLE
        binding.tvTwo.visibility = View.VISIBLE
        requireView().setBackgroundResource(R.color.white)
    }

    private fun setupTextViewClickListeners() {
        val tvOne = binding.tvOne
        val tvTwo = binding.tvTwo

        tvOne.setOnClickListener {
            gameSuffixViewModel.handleWord(tvWord.text.toString(), tvOne.text.toString(), 0)
        }

        tvTwo.setOnClickListener {
            gameSuffixViewModel.handleWord(tvWord.text.toString(), tvTwo.text.toString(), 1)
        }

        binding.bNextPage.setOnClickListener {
            gameSuffixViewModel.checkAnswer(tvWord.text.toString())
        }

        tvWord.setOnClickListener { gameSuffixViewModel.delete() }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}