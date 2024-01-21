package com.example.mypetproject2.features.ui.games.punctuationgames

import android.content.Context
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.listPunctuationGameTwo
import com.example.mypetproject2.databinding.FragmentPunctuationGameOneBinding

class PunctuationGameOneFragment : Fragment() {

    private var _binding: FragmentPunctuationGameOneBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: GamePunctuationViewModel
    private lateinit var tvWord: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPunctuationGameOneBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GamePunctuationViewModel::class.java]
        tvWord = binding.tvWord



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        navigationArguments()
        setupTextViewClickListeners()
    }

    private fun navigationArguments() {

        when (arguments?.getInt("gameNumber") ?: 0) {
            1 -> initGame(listPunctuationGameTwo, requireContext(), tvWord)
            2 -> tvWord.text = "Вторая"
            3 -> tvWord.text = "Третья"
            4 -> tvWord.text = "Четвертая"
        }
    }

    fun initObserver() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStatePunctuation.NewWord -> {
                    tvWord.text = it.word
                    tvWord.movementMethod = LinkMovementMethod.getInstance()
                    binding.bCheckAnswer.isEnabled = true
                    resetViewState()
                }
                is GameStatePunctuation.CheckWord -> {
                    tvWord.toString().replace("▢", "")
                    val lastAnswer = it.state.answers.last()
                    val isCorrect = lastAnswer.first.replace("!", "") == lastAnswer.second.replace("▢", "")

                    binding.bCheckAnswer.isEnabled = false
                    val id = if (isCorrect) {
                        R.color.green_light
                    }else {
                        R.color.red_light
                    }
                    requireView().setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            id
                        )
                    )

                    when (arguments?.getInt("gameNumber") ?: 0) {
                        1 -> viewModel.delay(listPunctuationGameTwo, requireContext(), tvWord)
                        2 -> tvWord.text = "Вторая"
                        3 -> tvWord.text = "Третья"
                        4 -> tvWord.text = "Четвертая"
                    }


                }
                is GameStatePunctuation.FinishGame -> {

                }
            }
        }

    }

    private fun setupTextViewClickListeners() {
        binding.bCheckAnswer.setOnClickListener {
            viewModel.checkAnswer(tvWord.text.toString())
        }
        tvWord.setOnClickListener {
            viewModel.delete()
        }
    }
    private fun resetViewState() {
        requireView().setBackgroundResource(R.color.white)
    }

    private fun initGame(list: List<String>, context: Context, textView: TextView) {
        viewModel.initGame(list, context, textView)
    }
}