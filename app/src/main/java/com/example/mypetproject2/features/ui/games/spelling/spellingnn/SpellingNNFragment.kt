package com.example.mypetproject2.features.ui.games.spelling.spellingnn

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
import androidx.lifecycle.ViewModelProvider
import com.example.mypetproject2.R
import com.example.mypetproject2.data.spellingNN
import com.example.mypetproject2.data.spellingSuffix
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.databinding.FragmentSpellingNNBinding
import com.example.mypetproject2.features.ui.games.spelling.setupOnBackPressedCallback
import com.example.mypetproject2.features.ui.games.spelling.transformWord
import com.example.mypetproject2.features.ui.games.stress.GameState
import com.example.mypetproject2.features.ui.games.stress.GamesFragment
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.utils.navigateSpellingToGameFinishedFragment

private var wordIndex: Int = 0
fun main() {

//    val spellingNNList = spellingNN.toList()
//    val randomWord = spellingNNList.random()
//
////        viewModel.checkWord(words)
//
//
//    val modifiedWord = randomWord.replace("Н+".toRegex(), "_")
//
//    println("$randomWord $modifiedWord")


    val a = spellingSuffix.toSet()
    a.forEach {
        println("\"$it\",")
    }


}

class SpellingNNFragment : Fragment() {

    private var wordIndex: Int = 0
    private lateinit var viewModel: GamesViewModel
    private lateinit var tvWord: TextView
    private var _binding: FragmentSpellingNNBinding? = null
    private val binding get() = _binding!!
    private var words: String = ""

    private var displayedWord: StringBuilder = StringBuilder()
    private var isLetterRemoved = false
    private var isUnderscorePresent = false

    private var isNextButtonEnabled = true


    private val DELAY_MILLIS = 1000L

    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable { showNextWord() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSpellingNNBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[GamesViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()
        initGame()

        initializeViews()
//        generateRandomWord()
//        displayWord()

        setupTextViewClickListeners()
//        setupNextPageButtonListener()
        setupOnBackPressedCallback()
    }

    private fun initGame() {
        viewModel.initGame()
    }

    private fun initObservers() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when(it) {
                is GameState.NewWord -> {
                    tvWord.text = it.word
                    resetViewState()
                    binding.bNextPage.isEnabled = false
                    binding.tvOneN.isEnabled = true
                    binding.tvTwoN.isEnabled = true
                }
                is GameState.UpdateWord -> {
                    tvWord.text = it.word
                    binding.tvOneN.isVisible = it.button != 0
                    binding.tvTwoN.isVisible = it.button != 1

                    binding.bNextPage.isEnabled = true
                }
                is GameState.CheckedAnswer -> {
                    binding.bNextPage.isEnabled = false
                    binding.tvOneN.isEnabled = false
                    binding.tvTwoN.isEnabled = false

                    val id = if (it.isCorrect) {
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
                    viewModel.delay()
                }
            }
        }
    }

    /**
     *Инициализирует viewModel и tvWord
     * */
    private fun initializeViews() {
        tvWord = binding.spellingWord
    }

    /**
     *  Генерирует случайное слово из доступного списка spelling и строит displayedWord,
     *  заменяя заглавные буквы на символ подчеркивания _
     * */
//    private fun generateRandomWord() {
//        val spellingNNList = spellingNN.toList()
//        val randomWord = spellingNNList.random()
//        words = randomWord
//
////        viewModel.checkWord(words)
//
//        displayedWord.clear()
//        isUnderscorePresent = false
//
//        val modifiedWord = randomWord.replace("Н+".toRegex(), "_")
//
//        var isReplaced = false
//        for (i in words.indices) {
//            val letter = words[i]
//
//            if (letter.isUpperCase()) {
//                if (!isReplaced) {
//                    displayedWord.append('_')
//                    isReplaced = true
//                    isUnderscorePresent = true
//                }
//            } else {
//                displayedWord.append(letter)
//            }
//        }
//    }

    /**
     *Отображает слово displayedWord в tvWord.
     * */
//    private fun displayWord() {
//        val finalWord = displayedWord.toString()
//        tvWord.text = finalWord
//    }

    /**
     *Устанавливает обработчики событий для нажатий на текстовые поля tvOne, tvTwo и tvWord.
     * */
    private fun setupTextViewClickListeners() {
        val tvOne = binding.tvOneN
        val tvTwo = binding.tvTwoN

        tvOne.setOnClickListener {
            viewModel.handleWord(tvWord.text.toString(), tvOne.text.toString(), 0)
//            handleLetterClick(tvOne)
        }

        tvTwo.setOnClickListener {
            viewModel.handleWord(tvWord.text.toString(), tvTwo.text.toString(), 1)
//            handleLetterClick(tvTwo)
        }
        binding.bNextPage.setOnClickListener {
//            setupNextPageButtonListener()
            viewModel.checkAnswer(tvWord.text.toString())
        }

//        tvWord.setOnClickListener {
//            handleTvWordClick()
//        }
    }

    /**
     *Обрабатывает нажатие на текстовое поле с выбранной буквой.
     *  Заменяет символ подчеркивания _ в tvWord на выбранную букву,
     *  скрывает текущее текстовое поле и показывает другое текстовое поле.
     * */
    private fun handleLetterClick(selectedLetterTextView: TextView) {
        val selectedLetter = selectedLetterTextView.text.toString()
        val underscoreIndex = tvWord.text.indexOf('_')
        if (underscoreIndex != -1) {
            val updatedWord = tvWord.text.replaceRange(underscoreIndex, underscoreIndex + 1, selectedLetter)
            tvWord.text = updatedWord

            selectedLetterTextView.visibility = View.GONE
        }

        binding.bNextPage.isEnabled = !tvWord.text.contains("_")
    }

    /**
     *Обрабатывает нажатие на tvWord.
     *  Заменяет все буквы в tvWord на символы подчеркивания _
     *  и сбрасывает видимость текстовых полей tvOne и tvTwo
     * */
    private fun handleTvWordClick() {
        val word = displayedWord.toString()
        val updatedWord = StringBuilder()

        for (i in word.indices) {
            val letter = word[i]
            if (letter.isUpperCase()) {
                updatedWord.append('_')
            } else {
                updatedWord.append(letter)
            }
        }

        tvWord.text = updatedWord.toString()

        resetViewState()

        isLetterRemoved = false

        binding.bNextPage.isEnabled = !tvWord.text.contains("_")


    }

    /**
     *Устанавливает обработчик события для кнопки bNextPage, которая выполняет проверку ответа пользователя.
     * */
    private fun setupNextPageButtonListener() {
        binding.bNextPage.setOnClickListener {
            if (isNextButtonEnabled) {
                isNextButtonEnabled = false
                it.isEnabled = false
                val userAnswer = tvWord.text.toString()
                viewModel.getWordCount(userAnswer)
                checkAnswer(userAnswer)

                viewModel.wordCountLiveData.observe(viewLifecycleOwner) { count ->
                    val isCorrect = userAnswer.equals(words, ignoreCase = true)
                    val newCount = if (isCorrect) count + 1 else 0

                    viewModel.insertWordToAllWords(transformWord(words), newCount)
                }
            }
        }
    }

    /**
     *  Проверяет ответ пользователя, сравнивая его с правильным ответом (randomWord).
     *  Изменяет цвет фона представления в соответствии с правильностью ответа.
     *  Запускает отложенную задачу (runnable) для отображения следующего слова.
     * */
    private fun checkAnswer(userAnswer: String) {
        val isCorrect = words == userAnswer
        viewModel.updateScore(isCorrect)
        viewModel.addUserAnswer(isCorrect)
        viewModel.setUserAnswers(userAnswer)

        if (userAnswer.equals(words, ignoreCase = true)) {
            requireView().setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.green_light
                )
            )
        } else {
            requireView().setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.red_light
                )
            )
        }

        handler.postDelayed(runnable, DELAY_MILLIS)
    }

    /**
     *Отображает следующее слово.
     *  Если wordIndex достигает значения 5, вызывает функцию showGameResults и сбрасывает игру.
     *  В противном случае, генерирует новое случайное слово, отображает его и восстанавливает состояние представлений.
     * */
    private fun showNextWord() {
        wordIndex++
        if (wordIndex >= GamesFragment.MAX_ATTEMPTS) {
            showGameResults()
            resetGame()
        } else {
//            generateRandomWord()
//            displayWord()
            resetViewState()
            binding.bNextPage.isEnabled = !isUnderscorePresent
        }

        isNextButtonEnabled = true
    }


    /**
     *Сбрасывает состояние игры, возвращая цвет фона в исходное состояние и очищая randomWord.
     * */
    private fun resetGame() {
        requireView().setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                android.R.color.transparent
            )
        )
        words = ""
    }

    /**
     *Восстанавливает видимость текстовых полей textView1 и textView2, а также цвет фона.
     * */
    private fun resetViewState() {
        binding.tvOneN.visibility = View.VISIBLE
        binding.tvTwoN.visibility = View.VISIBLE
        requireView().setBackgroundResource(R.color.white)
    }

    /**
     * Возвращает ответы пользователя в виде массива Boolean
     * */
    private fun getUserAnswers(): BooleanArray {
        val userAnswersList = viewModel.userAnswers.value ?: mutableListOf()
        return userAnswersList.toBooleanArray()
    }

    /**
     * Отображает результаты игры, вычисляет процент правильных ответов,
     *  получает ответы пользователя и историю ответов, а затем переходит к фрагменту GameFinishedFragment,
     *  передавая необходимые данные.
     * */

    // SpellingNNFragment
    private fun showGameResults() {
        val percentage = calculatePercentage()
        val userAnswers = getUserAnswers()
        val userAnswerHistory = viewModel.userAnswersHistory.value?.toTypedArray()!!
        Log.d(
            "showGameResults",
            "userAnswerHistory $userAnswerHistory"
        )
        navigateSpellingToGameFinishedFragment(
            viewModel.score.value ?: 0,
            percentage,
            userAnswers,
            userAnswerHistory,
            "spelling"
        )
    }

    /**
     *Вычисляет процент правильных ответов в игре.
     * */
    private fun calculatePercentage(): Float {
        return (viewModel.score.value?.toFloat() ?: 0f) / GamesFragment.MAX_ATTEMPTS * 100
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacks(runnable)
    }
}

