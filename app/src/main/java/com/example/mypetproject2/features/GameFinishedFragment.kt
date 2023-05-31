package com.example.mypetproject2.features

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mypetproject2.AnswerHistoryAdapter
import com.example.mypetproject2.AnswerHistoryItem
import com.example.mypetproject2.R
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentGameFinishedBinding
import com.example.mypetproject2.features.ui.games.GamesFragment
import java.util.*

class GameFinishedFragment : Fragment() {

    private var _binding: FragmentGameFinishedBinding? = null
    private val binding get() = _binding!!

    private var score: Int = 0

    private var selectedVowelIndex: Int = -1

    private var selectedVowelChar: Char? = null
    private var percentage: Float = 0f

    private var wordIndex: Int = 0

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
//        val correctAnswersText = getString(R.string.count_of_right_answers, correctAnswers, totalWords)
//        binding.tvRightAnswers.text = correctAnswersText

//        binding.tvScore.text = getString(R.string.score_answers, score.toString())
//        binding.tvScorePercentage.text = getString(R.string.score_percentage, percentage.toString())

        // Получение списка элементов истории ответов
        val answerHistoryItems = getAnswerHistoryItems()
        val answerHistoryAdapter = AnswerHistoryAdapter(answerHistoryItems)

        binding.rvHistoryAnswer.adapter = answerHistoryAdapter

        val recyclerView: RecyclerView = rootView.findViewById(R.id.rv_history_answer)

        // Создайте и установите LayoutManager
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) // Замените requireContext() на ваш контекст
        recyclerView.layoutManager = layoutManager

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Метод для формирования списка элементов истории ответов
    private fun getAnswerHistoryItems(): List<AnswerHistoryItem> {
        val answerHistoryList = mutableListOf<AnswerHistoryItem>()

        val endIndex = minOf(stress.size, 15) // Ограничиваем количество слов до 15 или размера массива stress, если он меньше
        for (i in 0 until endIndex) {
            val word = stress[i]
            val isCorrect = isAnswerCorrect(i) // передаем индекс i вместо wordIndex

            val answerItem = AnswerHistoryItem(word, isCorrect)
            answerHistoryList.add(answerItem)
        }

        return answerHistoryList
    }

    private fun isVowel(c: Char): Boolean {
        val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
        return c.lowercaseChar() in vowels
    }

    private fun isAnswerCorrect(index: Int): Boolean {
        val word = stress[index]
        val correctIndex = word.indexOfFirst { it.isUpperCase() }

        val vowelIndices = mutableListOf<Int>()

        for (i in 0 until word.length) {
            val character = word[i]
            if (isVowel(character)) {
                vowelIndices.add(i)
            }
        }

        return vowelIndices.contains(correctIndex)
    }
}
