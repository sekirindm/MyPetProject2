package com.example.mypetproject2.features.ui.games.spelling.spellingpref

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentSpellingPrefBinding
import com.example.mypetproject2.utils.setupOnBackPressedCallback
import com.example.mypetproject2.utils.navigateSpellingPrefToGameFinishedFragment
import kotlinx.coroutines.cancel


class SpellingPrefFragment : Fragment() {

    private var _binding: FragmentSpellingPrefBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvWord: TextView
    private lateinit var gamePrefViewModel: GamePrefViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSpellingPrefBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initializeViews()
        initObservers()
        initGame()
        setupTextViewClickListeners()
        setupOnBackPressedCallback()

//       "сделать два метода, один для clicklisteners, другой для observer")
        return rootView
    }

    private fun initializeViews() {
        gamePrefViewModel = ViewModelProvider(this)[GamePrefViewModel::class.java]
        tvWord = binding.tvSpellingPref
    }

    private fun initGame() {
        gamePrefViewModel.initGame()
    }

    private fun initObservers() {
        gamePrefViewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStatePref.NewWord -> {
                    tvWord.text = it.word
                    resetViewState()
                    binding.tvOne.text = it.letters[0]
                    binding.tvTwo.text = it.letters[1]
                    binding.bNextPage.isEnabled = false
                    binding.tvOne.isEnabled = true
                    binding.tvTwo.isEnabled = true
                }

                is GameStatePref.UpdateWord -> {
                    tvWord.text = it.word
                    binding.tvOne.isVisible = it.button != 0
                    binding.tvTwo.isVisible = it.button != 1
                    binding.bNextPage.isEnabled = true
                }

                is GameStatePref.CheckedAnswer -> {
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
                    gamePrefViewModel.delay()
                    gamePrefViewModel.updateScore(isCorrect)
                }

                is GameStatePref.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswers = state.answers.map { pair -> pair.first == pair.second }.toBooleanArray()
                    val userAnswersHistory = state.answers.map { it.second }.toTypedArray()
                    navigateSpellingPrefToGameFinishedFragment(
                        gamePrefViewModel.score.value ?: 0,
                        percentage,
                        userAnswers,
                        userAnswersHistory,
                        "spellingpref"
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
            gamePrefViewModel.handleWord(tvWord.text.toString(), tvOne.text.toString(), 0)
        }

        tvTwo.setOnClickListener {
            gamePrefViewModel.handleWord(tvWord.text.toString(), tvTwo.text.toString(), 1)
        }
        binding.bNextPage.setOnClickListener {
            gamePrefViewModel.checkAnswer(tvWord.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        lifecycleScope.cancel()
    }
}