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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
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
import com.example.mypetproject2.utils.navigateSpellingPrefToGameFinishedFragment
import com.example.mypetproject2.utils.navigateSpellingRootToGameFinishedFragment
import java.util.Random

class SpellingRootFragment : Fragment() {
    private var _binding: FragmentSpellingRootBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvWord: TextView
    private lateinit var gamesRootViewModel: GameRootViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSpellingRootBinding.inflate(inflater, container, false)
        val rootView = binding.root

        initializeViews()
        initGame()
        initObserver()
        setupTextViewClickListeners()
        setupOnBackPressedCallback()

        return rootView
    }

    private fun initializeViews() {
        gamesRootViewModel = ViewModelProvider(this)[GameRootViewModel::class.java]
        tvWord = binding.tvSpellingPref
    }

    private fun initGame() {
        gamesRootViewModel.initGame()
    }

   private fun initObserver() {
        gamesRootViewModel.gameState.observe(viewLifecycleOwner) {
            when(it) {
                is GameStateRoot.NewWord -> {
                    tvWord.text = it.word
                    resetViewState()
                    binding.tvOne.text = it.letters[0]
                    binding.tvTwo.text = it.letters[1]
                    binding.bNextPage.isEnabled = false
                    binding.tvOne.isEnabled = true
                    binding.tvTwo.isEnabled = true
                }
                is GameStateRoot.UpdateWord -> {
                    tvWord.text = it.word
                    binding.tvOne.isVisible = it.button != 0
                    binding.tvTwo.isVisible = it.button != 1
                    binding.bNextPage.isEnabled = true
                }
                is GameStateRoot.CheckedAnswer -> {
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
                            requireContext(),
                            id
                        )
                    )
                    gamesRootViewModel.delay()
                }
                is GameStateRoot.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswers = state.answers.map { pair -> pair.first == pair.second }.toBooleanArray()
                    val userAnswersHistory = state.answers.map { it.second }.toTypedArray()
                    navigateSpellingRootToGameFinishedFragment(
                        gamesRootViewModel.score.value ?: 0,
                        percentage,
                        userAnswers,
                        userAnswersHistory,
                        "spellingroot"
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
            gamesRootViewModel.handleWord(tvWord.text.toString(), tvOne.text.toString(), 0)
        }

        tvTwo.setOnClickListener {
            gamesRootViewModel.handleWord(tvWord.text.toString(), tvTwo.text.toString(), 1)
        }

        binding.bNextPage.setOnClickListener {
            gamesRootViewModel.checkAnswer(tvWord.text.toString())
        }

        tvWord.setOnClickListener {
            gamesRootViewModel.delete()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}