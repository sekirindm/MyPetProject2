package com.example.mypetproject2.features.ui.games.choosecorrectword.gamenumber14

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding
import com.example.mypetproject2.databinding.FragmentGameFourteenBinding
import com.example.mypetproject2.utils.navigateGameFourteenFragmentToFinishedFragment
import com.example.mypetproject2.utils.setupOnBackPressedCallback

class GameFourteenFragment : Fragment() {

    var _binding: FragmentGameFourteenBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: GameFourteenViewModel
    private lateinit var tvWord: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFourteenBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGame()
        observeGame()
        setupButtonClickListeners()
        setupOnBackPressedCallback()
    }

    fun observeGame() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStateFourteen.NewWord -> {
                    requireView().setBackgroundResource(R.color.white)
                    tvWord.text = it.word
                    binding.button.isEnabled = true
                    binding.button4.isEnabled = true
                    binding.button5.isEnabled = true
                }

                is GameStateFourteen.CheckAnswer -> {
                    binding.button.isEnabled = false
                    binding.button4.isEnabled = false
                    binding.button5.isEnabled = false

                    val lastAnswer = it.state.answers.last()
                    val isCorrect = lastAnswer.first == lastAnswer.second
                    val id = if (isCorrect) R.color.green_light else R.color.red_light
                    requireView().setBackgroundColor(ContextCompat.getColor(requireContext(), id))
                    viewModel.delay()
                    viewModel.updateScore(isCorrect)
                }

                is GameStateFourteen.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswers = state. answers.map { pair ->  pair.second == pair.first}.toBooleanArray()
                    val userAnswersHistory = state.answers.map { it.second }.toTypedArray()
                    navigateGameFourteenFragmentToFinishedFragment(
                        viewModel.score.value ?: 0,
                        percentage,
                        userAnswers,
                        userAnswersHistory,
                        "fourteen"
                    )
                }
            }
        }
    }

    private fun initGame() {
        viewModel.initGame()
    }

    private fun setupButtonClickListeners() {
        binding.button.setOnClickListener { viewModel.checkAnswer(tvWord.text.toString(), 2) }
        binding.button4.setOnClickListener { viewModel.checkAnswer(tvWord.text.toString(), 1) }
        binding.button5.setOnClickListener { viewModel.checkAnswer(tvWord.text.toString(), 0) }
    }

    private fun initialize() {
        viewModel = ViewModelProvider(this)[GameFourteenViewModel::class.java]
        tvWord = binding.tvWord
    }
}