package com.example.mypetproject2.features

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mypetproject2.R
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding!!

    private var score: Int = 0
    private var percentage: Float = 0f

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        val rootView = binding.root

        score = arguments?.getInt("score") ?: 0
        percentage = arguments?.getFloat("percentage") ?: 0f

        val totalWords = 15 // Общее количество слов
        val correctAnswers = (score + totalWords) / 2 // Расчет количества правильных ответов
        val correctAnswersText = getString(R.string.count_of_right_answers, correctAnswers, totalWords)
        binding.tvRightAnswers.text = correctAnswersText

        binding.tvScore.text = getString(R.string.score_answers, score.toString())
        binding.tvScorePercentage.text = getString(R.string.score_percentage, percentage.toString())

        return rootView
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}