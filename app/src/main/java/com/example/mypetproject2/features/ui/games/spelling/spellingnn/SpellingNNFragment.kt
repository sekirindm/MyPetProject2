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
import com.example.mypetproject2.data.spellingRoot
import com.example.mypetproject2.databinding.FragmentSpellingNNBinding
import com.example.mypetproject2.features.ui.games.spelling.calculatePercentage
import com.example.mypetproject2.features.ui.games.spelling.getUserAnswers
import com.example.mypetproject2.features.ui.games.spelling.setupOnBackPressedCallback
import com.example.mypetproject2.features.ui.games.spelling.transformWord
import com.example.mypetproject2.features.ui.games.stress.GameState
import com.example.mypetproject2.features.ui.games.stress.StressFragment
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


    val a = spellingRoot.toSet()
    a.forEach {
        println("\"${it.trim()}\",")
    }
}

class SpellingNNFragment : Fragment() {

    private lateinit var viewModel: GamesViewModel
    private lateinit var tvWord: TextView
    private var _binding: FragmentSpellingNNBinding? = null
    private val binding get() = _binding!!

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
        setupTextViewClickListeners()
        setupOnBackPressedCallback()
    }

    private fun initGame() {
        viewModel.initGame()
    }

    /**
     *
     * Данный метод обрабатывает все состояния нашей игры
     * */
    private fun initObservers() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            when(it) {
                is GameState.NewWord -> { // начало игры
                    tvWord.text = it.word  // тут у нас выбранное рандомно слово, которое мы упаковывали в NewWord
                    resetViewState()  // восстанавливаем состояние экрана в начале каждой итерации, т.е. при каждом новом слове

                    binding.bNextPage.isEnabled = false
                    binding.tvOneN.isEnabled = true
                    binding.tvTwoN.isEnabled = true
                }
                is GameState.UpdateWord -> { //
                    tvWord.text = it.word
                    binding.tvOneN.isVisible = it.button != 0
                    binding.tvTwoN.isVisible = it.button != 1

                    binding.bNextPage.isEnabled = true
                }
                is GameState.CheckedAnswer -> { //

                    val lastAnswer = it.state.answers.last()
                    val isCorrect = lastAnswer.first == lastAnswer.second

                    Log.i("TAGG", "state ${it.state}")
                    binding.bNextPage.isEnabled = false
                    binding.tvOneN.isEnabled = false
                    binding.tvTwoN.isEnabled = false

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
                    viewModel.delay()
                }
                is GameState.FinishGame -> {
                    val state = it.state
                    val percentage = state.score / 5f * 100f
                    val userAnswers = state.answers.map { pair -> pair.first == pair.second }.toBooleanArray()
                    val userAnswerHistory = state.answers.map { it.second }.toTypedArray()
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
     *Устанавливает обработчики событий для нажатий на текстовые поля tvOne, tvTwo и tvWord.
     * */
    private fun setupTextViewClickListeners() {
        val tvOne = binding.tvOneN
        val tvTwo = binding.tvTwoN

        tvOne.setOnClickListener { // нажали на одну Н
            viewModel.handleWord(tvWord.text.toString(), tvOne.text.toString(), 0)
        }

        tvTwo.setOnClickListener { // нажали на НН
            viewModel.handleWord(tvWord.text.toString(), tvTwo.text.toString(), 1)
        }
        binding.bNextPage.setOnClickListener {
            viewModel.checkAnswer(tvWord.text.toString())
        }
        tvWord.setOnClickListener {
            viewModel.delete()
        }
    }

    /**
     *Восстанавливает видимость текстовых полей textView1 и textView2, а также цвет фона.
     * */
    private fun resetViewState() {
        binding.tvOneN.visibility = View.VISIBLE
        binding.tvTwoN.visibility = View.VISIBLE
        requireView().setBackgroundResource(R.color.white)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

