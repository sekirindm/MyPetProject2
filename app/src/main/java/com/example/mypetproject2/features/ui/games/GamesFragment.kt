package com.example.mypetproject2.features.ui.games

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentGamesBinding
import com.example.mypetproject2.features.getPair
import com.example.mypetproject2.features.isVowel
import java.util.*

class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvWord: TextView
    private lateinit var spannableStringBuilder: SpannableStringBuilder

    private lateinit var viewModel: GamesViewModel

    private var wordIndex: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        val rootView = binding.root

        viewModel = ViewModelProvider(this).get(GamesViewModel::class.java)

        initializeViews()

        return rootView
    }


//  initializeViews(): Инициализирует необходимые представления и настраивает обработчик клика для кнопки "Проверить".
//  Также вызывается метод setupWordClick() и устанавливается LinkMovementMethod для TextView tvWord.
    private fun initializeViews() {
        tvWord = binding.tvWord
        binding.bCheck.setOnClickListener { checkWordTest() }

        setupWordClick()
        tvWord.movementMethod = LinkMovementMethod.getInstance()
    }

   // setupWordClick(): Устанавливает текущее слово в tvWord и создает SpannableStringBuilder
    // с помощью метода createSpannableStringBuilder(word).

    private fun setupWordClick() {
        val word = stress[wordIndex]
        spannableStringBuilder = createSpannableStringBuilder(word)

        binding.tvWord.text = spannableStringBuilder
    }

    // createSpannableStringBuilder(word): Создает SpannableStringBuilder для заданного слова,
    // где каждая гласная буква делается кликабельной с помощью ClickableSpan.

    private fun createSpannableStringBuilder(word: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(word.lowercase(Locale.getDefault()))

        for (characterIndex in word.indices) {
            val character = word[characterIndex]
            if (isVowel(character)) {
                val clickableSpan = createClickableSpan(characterIndex, character)
                builder.setSpan(
                    clickableSpan,
                    characterIndex,
                    characterIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

        return builder
    }

    // createClickableSpan(characterIndex, character): Создает ClickableSpan для заданного индекса и символа гласной буквы.
    // При клике на эту букву будет вызываться метод handleVowelClick(characterIndex, character).
    private fun createClickableSpan(characterIndex: Int, character: Char): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(view: View) {
                handleVowelClick(characterIndex, character)
            }
        }
    }

    //handleVowelClick(characterIndex, character): Обрабатывает клик на гласную букву.
    // Устанавливает выбранный индекс и символ гласной буквы в ViewModel и обновляет форматирование выбранной гласной буквы
    // с помощью метода updateSelectedVowelFormatting().
     fun handleVowelClick(characterIndex: Int, character: Char) {
        viewModel.setSelectedVowelIndex(characterIndex)
        viewModel.setSelectedVowelChar(character)
        updateSelectedVowelFormatting()
    }

//    updateSelectedVowelFormatting(): Обновляет форматирование выбранной гласной буквы в SpannableStringBuilder.
//    Предыдущая выбранная гласная буква возвращается в нижний регистр, а новая выбранная гласная буква выделяется жирным шрифтом и становится черной.
//    Обновленный SpannableStringBuilder устанавливается в tvWord.
    private fun updateSelectedVowelFormatting() {
        val selectedVowelIndex = viewModel.selectedVowelIndex.value ?: -1
        val selectedVowelChar = viewModel.selectedVowelChar.value

        val spannableLength = spannableStringBuilder.length
        if (selectedVowelIndex in 0 until spannableLength && selectedVowelChar != null) {
            val character = selectedVowelChar.toString().uppercase()

            val previousVowelIndex = viewModel.previousVowelIndex
            if (previousVowelIndex in 0 until spannableLength) {
                val previousVowelChar = spannableStringBuilder[previousVowelIndex]
                spannableStringBuilder.removeSpan(ForegroundColorSpan(Color.BLACK))
                spannableStringBuilder.replace(previousVowelIndex, previousVowelIndex + 1, previousVowelChar.lowercase().toString())
            }

            spannableStringBuilder.setSpan(
                ForegroundColorSpan(Color.BLACK),
                selectedVowelIndex,
                selectedVowelIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableStringBuilder.replace(selectedVowelIndex, selectedVowelIndex + 1, character)

            tvWord.text = spannableStringBuilder
            viewModel.previousVowelIndex = selectedVowelIndex
        }
    }

    //getUserAnswers(): Возвращает массив с ответами пользователя на текущие вопросы в формате BooleanArray.
    private fun getUserAnswers(): BooleanArray {
        val userAnswersList = viewModel.userAnswers.value ?: mutableListOf()
        return userAnswersList.toBooleanArray()
    }


//showNextWord(): Отображает следующее слово, если выбрана гласная буква. Индекс слова увеличивается на 1,
// и вызывается метод setupWordClick() для нового слова.
// Также сбрасываются выбранный индекс и символ гласной буквы в ViewModel.
    private fun showNextWord() {
        if (viewModel.selectedVowelIndex.value != -1) {
            wordIndex = (wordIndex + 1) % stress.size
            setupWordClick()
            viewModel.setSelectedVowelIndex(-1)
            viewModel.setSelectedVowelChar(null)
        }
    }


// checkWordTest(): Проверяет выбранную гласную букву с правильным ответом.
// Обновляет счет, количество попыток и добавляет ответ пользователя в ViewModel.
// Затем создает SpannableStringBuilder с результатами и обновляет tvWord.
// Если количество попыток достигает максимального значения, вызывается метод showGameResults(),
// иначе вызывается метод showNextWord() через 1 секунду.
    private fun checkWordTest() {
        if (viewModel.selectedVowelIndex.value != -1) {
            val word = stress[wordIndex]
            val correctIndex = stress[wordIndex].indexOfFirst { it.isUpperCase() }

            val isCorrect = correctIndex == viewModel.selectedVowelIndex.value
            viewModel.updateScore(isCorrect)
            viewModel.incrementTotalAttempts()
            viewModel.addUserAnswer(isCorrect)
            viewModel.setUserAnswers(word)

            val resultSpannableStringBuilder = createResultSpannableStringBuilder(word, correctIndex)

            spannableStringBuilder = resultSpannableStringBuilder
            tvWord.text = spannableStringBuilder

            if ((viewModel.totalAttempts.value ?: 0) >= MAX_ATTEMPTS) {
                showGameResults()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    showNextWord()
                }, 1000)
            }
        }
    }

//    createResultSpannableStringBuilder(word, correctIndex): Создает SpannableStringBuilder для отображения результатов теста.
//    Выделяет правильную и неправильную гласные буквы в зеленый и красный цвета соответственно. Также выделяет выбранную гласную букву.
    private fun createResultSpannableStringBuilder(word: String, correctIndex: Int): SpannableStringBuilder {
        val builder = SpannableStringBuilder(word.lowercase(Locale.ROOT))

        for (i in word.indices) {
            val character = word[i]
            if (isVowel(character)) {
                val span = createForegroundColorSpan(i, correctIndex)
                builder.setSpan(span, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

         val selectedCharIndex = viewModel.selectedVowelIndex.value?.let {
            viewModel.selectedVowelChar.value?.let { it1 ->
                word.indexOf(
                    it1
                )
            }
        }
        val selectedChar = viewModel.selectedVowelChar.value
        selectedChar?.let {
            val color = if (selectedCharIndex == correctIndex) Color.GREEN else Color.RED

            if (selectedCharIndex != null && selectedCharIndex == viewModel.selectedVowelIndex.value) {
                builder.setSpan(
                    ForegroundColorSpan(color),
                    selectedCharIndex,
                    selectedCharIndex + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                builder.replace(
                    selectedCharIndex,
                    selectedCharIndex + 1,
                    selectedChar.toString().uppercase()
                )
            }
        }

        return builder
    }


//    createForegroundColorSpan(i, correctIndex):
//    Создает ForegroundColorSpan для форматирования гласной буквы в зеленый или красный цвет в зависимости от ее правильности.
    private fun createForegroundColorSpan(i: Int, correctIndex: Int): CharacterStyle {
        return if (i == correctIndex) {
            ForegroundColorSpan(Color.GREEN)
        } else {
            ForegroundColorSpan(Color.RED)
        }
    }

//    showGameResults(): Показывает результаты игры. Вычисляет процент правильных ответов и передает его,
//    счет и ответы пользователя в следующий фрагмент GameFinishedFragment с помощью навигации.

    private fun showGameResults() {
        val percentage = (viewModel.score.value?.toFloat() ?: 0f) / MAX_ATTEMPTS * 100

        val userAnswers = getUserAnswers()

        val userAnswersHistory = viewModel.userAnswersHistory.value ?: emptyList()
        val wordPairs = getPair(userAnswersHistory)

        val action =
            GamesFragmentDirections.actionNavigationDashboardToGameFinishedFragment(
                viewModel.score.value ?: 0,
                percentage,
                userAnswers,
                userAnswersHistory.toTypedArray()
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val MAX_ATTEMPTS = 3
    }
}