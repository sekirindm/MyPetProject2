package com.example.mypetproject2.features.ui.games.punctuationgames

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.baselist.listPunctuationGameFive
import com.example.mypetproject2.data.baselist.listPunctuationGameFour
import com.example.mypetproject2.data.baselist.listPunctuationGameThree
import com.example.mypetproject2.data.baselist.listPunctuationGameTwo
import com.example.mypetproject2.databinding.FragmentPunctuationGameOneBinding
import com.example.mypetproject2.utils.navigatePunctuationFragment
import com.example.mypetproject2.utils.setupOnBackPressedCallback

class PunctuationGameOneFragment : Fragment() {

    private var _binding: FragmentPunctuationGameOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GamePunctuationViewModel
    private lateinit var tvWord: TextView
    private lateinit var ivBack: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPunctuationGameOneBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GamePunctuationViewModel::class.java]
        tvWord = binding.tvWord
        ivBack = binding.imageView2
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (arguments?.getInt("gameNumber") ?: 0) {
            1 -> initGame(listPunctuationGameTwo, requireContext(), tvWord,  ivBack)
            2 -> initGame(listPunctuationGameThree, requireContext(), tvWord, ivBack)
            3 -> initGame(listPunctuationGameFour, requireContext(), tvWord, ivBack)
            4 -> initGame(listPunctuationGameFive, requireContext(), tvWord, ivBack)
        }
        initObserver()
        setupTextViewClickListeners()
        setupOnBackPressedCallback()
    }

    private fun initObserver() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStatePunctuation.NewWord -> {
                    tvWord.text = it.word
                    tvWord.movementMethod = LinkMovementMethod.getInstance()
                    binding.bCheckAnswer.isEnabled = true
                    resetViewState()
                }

                is GameStatePunctuation.CheckWord -> {
                    val lastAnswer = it.state.answers.last()
                    val isCorrect =
                        lastAnswer.first.replace("#", "") == lastAnswer.second.replace("▢", "")

                    binding.bCheckAnswer.isEnabled = false
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
                    when (arguments?.getInt("gameNumber") ?: 0) {
                        1 -> viewModel.delay(listPunctuationGameTwo, requireContext(), tvWord,  ivBack)
                        2 -> viewModel.delay(listPunctuationGameThree, requireContext(), tvWord, ivBack)
                        3 -> viewModel.delay(listPunctuationGameFour, requireContext(), tvWord, ivBack)
                        4 -> viewModel.delay(listPunctuationGameFive, requireContext(), tvWord, ivBack)
                    }
                    viewModel.updateScore(isCorrect)
                }

                is GameStatePunctuation.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswers =
                        state.answers.map { pair -> pair.first == pair.second }.toBooleanArray()
                    Log.d(
                        "userAnswerspair",
                        "${state.answers.map { it.first }}, ${state.answers.map { it.second }}"
                    )
                    val userAnswerHistory = state.answers.map { it.second }.toTypedArray()
                    navigatePunctuationFragment(
                        viewModel.score.value ?: 0,
                        percentage,
                        userAnswers,
                        userAnswerHistory,
                        "punctuation"
                    )
                }
            }
        }
    }

    private fun setupTextViewClickListeners() {
        binding.bCheckAnswer.setOnClickListener {
            viewModel.checkAnswer(tvWord.text.toString())
        }
    }

    private fun resetViewState() {
        requireView().setBackgroundResource(R.color.white)
    }

    private fun initGame(list: List<String>, context: Context, textView: TextView, imageView: ImageView) {
        viewModel.initGame(list, context, textView, imageView)
    }
}