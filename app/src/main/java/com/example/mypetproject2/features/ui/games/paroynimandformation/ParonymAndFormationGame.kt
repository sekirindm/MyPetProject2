package com.example.mypetproject2.features.ui.games.paroynimandformation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.chaos.view.PinView
import com.example.mypetproject2.R
import com.example.mypetproject2.data.baselist.paronymList
import com.example.mypetproject2.data.baselist.wordFormationList
import com.example.mypetproject2.databinding.FragmentParoynimGameBinding
import com.example.mypetproject2.utils.navigateParonymGameFragmentToFinishedFragment
import com.example.mypetproject2.utils.setupOnBackPressedCallback

class ParonymAndFormationGame : Fragment() {

    private var _binding: FragmentParoynimGameBinding? = null
    private val binding get() = _binding!!
    private lateinit var tvWord: TextView
    private lateinit var viewModel: GameParonymViewModel
    private lateinit var pvParonym: PinView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParoynimGameBinding.inflate(inflater, container, false)
        tvWord = binding.tvWords
        initializeViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initGame()
        initObservers()
        setListeners()
        setupOnBackPressedCallback()
    }

    private fun initializeViews() {
        viewModel = ViewModelProvider(this)[GameParonymViewModel::class.java]
        pvParonym = binding.pvParonym

        Log.d("pvParonym", arrayListOf(pvParonym.text.toString()).toString())
    }

    private fun initGame() {
        when (arguments?.getInt("gameNumber")?:0) {
            1 -> viewModel.initGame(pvParonym, paronymList)
            2 -> viewModel.initGame(pvParonym, wordFormationList)
        }
    }

    fun initObservers() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when (it) {
                is GameStateParonym.NewWord -> {
                    tvWord.text = it.word
                    resetBackgroundState()
                    binding.bNextPage.isEnabled = true
                }

                is GameStateParonym.CheckWord -> {
                    binding.bNextPage.isEnabled = false
                    val last = it.state.answers.last()
                    val rightAnswer = last.second == pvParonym.text.toString()
                    Log.d("rightAnswerFirst", last.second)
                    Log.d("rightAnswerSecond", pvParonym.text.toString())
                    val id = if (rightAnswer) {
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
                    when (arguments?.getInt("gameNumber")?:0) {
                        1 -> viewModel.delay(pvParonym, paronymList)
                        2 -> viewModel.delay(pvParonym, wordFormationList)
                    }
                    viewModel.updateScore(rightAnswer)
                }

                is GameStateParonym.FinishGame -> {

                    val state = it.state
                    val percentage = state.score / 5f * 100
                    val userAnswer = state.answers.map {pair -> pair.second == pair.first }

                    Log.d(
                        "userAnswer",
                        "${state.answers.map { it.second }}, ${state.answers.map { it.first }}"
                    )
                    val userAnswerHistory = state.answers.map { it.second }.toTypedArray()
                    navigateParonymGameFragmentToFinishedFragment(
                        viewModel.score.value ?: 0,
                        percentage,
                        userAnswer.toBooleanArray(),
                        userAnswerHistory,
                        "paronym"
                    )
                }
            }
        }
    }

    private fun setListeners() {
        binding.bNextPage.setOnClickListener { viewModel.checkAnswer(pvParonym) }
    }

    private fun resetBackgroundState() {
        requireView().setBackgroundColor(
            ContextCompat.getColor(requireContext(), android.R.color.transparent)
        )
        pvParonym.text = null
    }
}