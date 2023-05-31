package com.example.mypetproject2.features.ui.games

import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.R
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentGamesBinding
import com.example.mypetproject2.databinding.FragmentHomeBinding
import java.util.*

class GamesFragment : Fragment() {
    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvWord: TextView
    private var selectedVowelIndex: Int = -1
    private var wordIndex: Int = 0
    private lateinit var spannableStringBuilder: SpannableStringBuilder
    private var selectedVowelChar: Char? = null
    private var score: Int = 0
    private var totalAttempts: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        val rootView = binding.root

        tvWord = binding.tvWord
        val buttonCheck = binding.bCheck
        val buttonNextWord = binding.bNextWord
        buttonCheck.setOnClickListener { checkWordTest() }

        buttonNextWord.setOnClickListener { showNextWord() }

        clickOnWord()
        tvWord.text = spannableStringBuilder
        tvWord.movementMethod = LinkMovementMethod.getInstance()

        return rootView
    }

    private fun isVowel(c: Char): Boolean {
        val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
        return c.lowercaseChar() in vowels
    }

    private fun clickOnWord() {
        val word = stress[wordIndex]
        spannableStringBuilder = SpannableStringBuilder(word.lowercase(Locale.getDefault()))

        for (characterIndex in word.indices) {
            val character = word[characterIndex]
            if (isVowel(character)) {
                val clickableSpan = object : ClickableSpan() {
                    override fun onClick(view: View) {
                        if (selectedVowelIndex != -1) {
                            spannableStringBuilder.setSpan(
                                ForegroundColorSpan(Color.BLACK),
                                selectedVowelIndex,
                                selectedVowelIndex + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                            )
                            val lowercaseChar =
                                spannableStringBuilder[selectedVowelIndex].lowercaseChar()
                            spannableStringBuilder.replace(
                                selectedVowelIndex,
                                selectedVowelIndex + 1,
                                lowercaseChar.toString()
                            )
                        }

                        spannableStringBuilder.setSpan(
                            ForegroundColorSpan(Color.BLACK),
                            characterIndex,
                            characterIndex + 1,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )
                        val uppercaseChar = character.uppercaseChar()

                        spannableStringBuilder.replace(characterIndex, characterIndex + 1, uppercaseChar.toString())

                        selectedVowelIndex = characterIndex
                        selectedVowelChar = character

                        tvWord.text = spannableStringBuilder

                        binding.bNextWord.isEnabled = true // Включение кнопки "Дальше" после выбора гласной буквы
                    }
                }
                spannableStringBuilder.setSpan(clickableSpan, characterIndex, characterIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        binding.bNextWord.isEnabled = false
    }

    private fun showNextWord() {
        if (selectedVowelIndex != -1) {
            wordIndex = (wordIndex + 1) % stress.size // Получение нового индекса слова

            clickOnWord()

            binding.tvWord.text = spannableStringBuilder // Обновление отображения нового слова

            selectedVowelChar = null // Сброс выбранной гласной буквы
            selectedVowelIndex = -1 // Сброс индекса выбранной гласной буквы

            binding.bNextWord.isEnabled = false // Отключение кнопки "Дальше"
        }
    }

     fun checkWordTest() {
        if (selectedVowelIndex != -1) {
            val word = stress[wordIndex]
            val correctIndex = stress[wordIndex].indexOfFirst { it.isUpperCase() }

            val resultSpannableStringBuilder = SpannableStringBuilder(word.lowercase(Locale.ROOT))

            for (i in 0 until word.length) {
                val character = word[i]
                if (isVowel(character)) {
                    val span: CharacterStyle = if (i == correctIndex) {
                        ForegroundColorSpan(Color.GREEN) // Зеленый цвет для правильного ответа
                    } else {
                        ForegroundColorSpan(Color.RED) // Красный цвет для неправильного ответа
                    }
                    resultSpannableStringBuilder.setSpan(
                        span,
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            }

            selectedVowelChar?.let { selectedChar ->
                val selectedCharIndex = word.indexOf(selectedChar)
                val color = if (selectedCharIndex == correctIndex) Color.GREEN else Color.RED

                resultSpannableStringBuilder.setSpan(
                    ForegroundColorSpan(color),
                    selectedCharIndex,
                    selectedCharIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                resultSpannableStringBuilder.replace(
                    selectedCharIndex,
                    selectedCharIndex + 1,
                    selectedChar.toString().uppercase()
                )
            }

            tvWord.text = resultSpannableStringBuilder
            spannableStringBuilder = resultSpannableStringBuilder

            updateScore(correctIndex == selectedVowelIndex)
            totalAttempts++
            if (totalAttempts >= MAX_ATTEMPTS) {
                showGameResults()
            }
        }
    }

    private fun updateScore(isCorrect: Boolean) {
        if (isCorrect) {
            score++
        } else {
            score = maxOf(0, score - 1) // Очки не могут быть отрицательными
        }
    }

    private fun showGameResults() {
        val percentage = (score.toFloat() / totalAttempts.toFloat()) * 100
        val action = GamesFragmentDirections.actionNavigationDashboardToGameFinishedFragment(score,
            percentage)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        private const val MAX_ATTEMPTS = 15
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}