package com.example.mypetproject2.features.ui.games.stress

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentGamesBinding
import com.example.mypetproject2.features.createSpannableStringBuilder
import com.example.mypetproject2.features.ui.games.spelling.setupOnBackPressedCallback
import com.example.mypetproject2.features.ui.games.stress.logic.GamesLogic
import com.example.mypetproject2.features.ui.games.stress.logic.SpannableStringBuilderHelper
import com.example.mypetproject2.utils.navigateToGameFinishedFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*

class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    private lateinit var tvWord: TextView
    private lateinit var spannableStringBuilder: SpannableStringBuilder

    private lateinit var gamesLogic: GamesLogic

    private lateinit var viewModel: GamesViewModel

    private val random = Random()
    private var usedIndexes: MutableList<Int> = mutableListOf()

    private var wordIndex: Int = 0

    private lateinit var spannableStringBuilderHelper: SpannableStringBuilderHelper


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        gamesLogic = GamesLogic()
        spannableStringBuilderHelper = SpannableStringBuilderHelper()

        initializeViews()
       setupOnBackPressedCallback()

        return binding.root
    }

    /**
     * initializeViews(): Инициализирует необходимые представления и настраивает обработчик клика для кнопки "Проверить".
     * Также вызывается метод setupWordClick() и устанавливается LinkMovementMethod для TextView tvWord.
     * */

    private fun initializeViews() {
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]

        tvWord = binding.tvWord
        binding.bCheck.setOnClickListener {
            checkWord()
//            saveUserAnswers()
        }
        setupWordClick()
        tvWord.movementMethod = LinkMovementMethod.getInstance()
    }

//    private fun saveUserAnswers(userAnswers: List<String>) {
//        val selectedVowelIndex = viewModel.selectedVowelIndex.value
//        if (selectedVowelIndex != null && selectedVowelIndex < userAnswers.size) {
//            val userAnswerHistory = userAnswers[selectedVowelIndex]
//            viewModel.setUserAnswers(userAnswerHistory)
//        }
//    }


    /**
     * setupWordClick(): Устанавливает текущее слово в tvWord и создает SpannableStringBuilder
     *  с помощью метода createSpannableStringBuilder(word).
     * */
    private fun setupWordClick() {
        val word = getRandomWordFromList()
        spannableStringBuilder = createSpannableStringBuilder(word) { characterIndex, character ->
            handleVowelClick(characterIndex, character)
        }
        binding.tvWord.text = spannableStringBuilder
    }

    private fun getRandomWordFromList(): String {
        if (usedIndexes.size == stress.size) {
            // Если все индексы использованы, начинаем заново
            usedIndexes.clear()
        }

        var newIndex: Int
        do {
            newIndex = random.nextInt(stress.size)
        } while (newIndex in usedIndexes)

        usedIndexes.add(newIndex)
        wordIndex = newIndex

        return stress[wordIndex]
    }

    /**
     * handleVowelClick(characterIndex, character): Обрабатывает клик на гласную букву.
     * Устанавливает выбранный индекс и символ гласной буквы в ViewModel и обновляет форматирование выбранной гласной буквы
     * с помощью метода updateSelectedVowelFormatting().
     * */
    fun handleVowelClick(characterIndex: Int, character: Char) {
        viewModel.setSelectedVowelIndex(characterIndex)
        viewModel.setSelectedVowelChar(character)
        updateSelectedVowelFormatting()
    }

    /**
     * updateSelectedVowelFormatting(): Обновляет форматирование выбранной гласной буквы в SpannableStringBuilder.
     * Предыдущая выбранная гласная буква возвращается в нижний регистр, а новая выбранная гласная буква выделяется жирным шрифтом и становится черной.
     * Обновленный SpannableStringBuilder устанавливается в tvWord.
     * */
    private fun updateSelectedVowelFormatting() {
        val selectedVowelIndex = viewModel.selectedVowelIndex.value ?: -1
        val selectedVowelChar = viewModel.selectedVowelChar.value

        val spannableLength = spannableStringBuilder.length
        if (selectedVowelIndex in 0 until spannableLength && selectedVowelChar != null) {
            gamesLogic.updateSelectedVowelFormatting(
                spannableStringBuilder,
                selectedVowelIndex,
                selectedVowelChar
            )
            tvWord.text = spannableStringBuilder
        }
    }

    /**
     * getUserAnswers(): Возвращает массив с ответами пользователя на текущие вопросы в формате BooleanArray.
     * */
    private fun getUserAnswers(): BooleanArray {
        val userAnswersList = viewModel.userAnswers.value ?: mutableListOf()
        return userAnswersList.toBooleanArray()
    }

    /**
     * showNextWord(): Отображает следующее слово, если выбрана гласная буква. Индекс слова увеличивается на 1,
     * вызывается метод setupWordClick() для нового слова.
     * Также сбрасываются выбранный индекс и символ гласной буквы в ViewModel.
     * */
    private fun showNextWord() {
        if (viewModel.selectedVowelIndex.value != -1) {
            displayNextWord()
        }
    }

    fun initObserver() {

    }

    private fun displayNextWord() {
        wordIndex = (wordIndex + 1) % stress.size
        setupWordClick()
        viewModel.setSelectedVowelIndex(-1)
        viewModel.setSelectedVowelChar(null)
    }

    /**
     * checkWordTest(): Проверяет выбранную гласную букву с правильным ответом.
     * Обновляет счет, количество попыток и добавляет ответ пользователя в ViewModel.
     * Затем создает SpannableStringBuilder с результатами и обновляет tvWord.
     * Если количество попыток достигает максимального значения, вызывается метод showGameResults(),
     * иначе вызывается метод showNextWord() через 1 секунду.
     * */
    private fun checkWord() {
        val correctIndex = getCorrectIndex()
        val selectedVowelIndex = viewModel.selectedVowelIndex.value ?: -1
        val isCorrect = correctIndex == selectedVowelIndex

        if (selectedVowelIndex != -1) {
            val word = stress[wordIndex]

            updateWordCount(word, isCorrect)
            updateScoreAndAttempts(isCorrect)
            updateUI(word, correctIndex, selectedVowelIndex, isCorrect)

            if ((viewModel.totalAttempts.value ?: 0) >= MAX_ATTEMPTS) {
                showGameResults()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    showNextWord()
                }, 1000)
            }
        }
    }

    private fun getCorrectIndex(): Int {
        return stress[wordIndex].indexOfFirst { it.isUpperCase() }
    }

    private fun updateWordCount(word: String, isCorrect: Boolean) {
        viewModel.getWordCount(word)
        viewModel.wordCountLiveData.observe(viewLifecycleOwner) {
            val newCount = if (isCorrect) it + 1 else 0
            viewModel.insertWordToAllWords(word, newCount)
        }
    }

    private fun updateScoreAndAttempts(isCorrect: Boolean) {
        viewModel.updateScore(isCorrect)
        viewModel.incrementTotalAttempts()
        viewModel.addUserAnswer(isCorrect)
    }

    private fun updateUI(word: String, correctIndex: Int, selectedVowelIndex: Int, isCorrect: Boolean) {
        val resultSpannableStringBuilder =
            spannableStringBuilderHelper.createResultSpannableStringBuilder(
                word,
                correctIndex,
                selectedVowelIndex,
                viewModel.selectedVowelChar.value
            )

        viewModel.setUserAnswers(resultSpannableStringBuilder.toString())
        spannableStringBuilder = resultSpannableStringBuilder
        tvWord.text = spannableStringBuilder
    }



    /**
     * showGameResults(): Показывает результаты игры. Вычисляет процент правильных ответов и передает его,
     * счет и ответы пользователя в следующий фрагмент GameFinishedFragment с помощью навигации.
     * */

    // GamesFragment
    private fun showGameResults() {
        val percentage = calculatePercentage()
        val userAnswers = getUserAnswers()
        val userAnswerHistory = viewModel.userAnswersHistory.value?.toTypedArray()!!
        navigateToGameFinishedFragment(
            viewModel.score.value ?: 0,
            percentage,
            userAnswers,
            userAnswerHistory,
            "stress"
        )
    }

    private fun calculatePercentage(): Float {
        return (viewModel.score.value?.toFloat() ?: 0f) / MAX_ATTEMPTS * 100
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
//        navView.visibility = View.GONE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        val MAX_ATTEMPTS = 5
    }
}