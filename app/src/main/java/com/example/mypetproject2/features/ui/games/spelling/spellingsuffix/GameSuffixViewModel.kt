package com.example.mypetproject2.features.ui.games.spelling.spellingsuffix

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.spellingSuffix
import com.example.mypetproject2.features.ui.games.spelling.transformWord
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.State
import kotlinx.coroutines.launch

sealed class GameStateSuffix() {
    data class NewWord(val word: String, val letters: List<String>) : GameStateSuffix()

    data class UpdateWord(val word: String, val button: Int): GameStateSuffix()

    data class CheckedAnswer(val state: State) : GameStateSuffix()

    data class FinishGame(val state: State) : GameStateSuffix()
}

class GameSuffixViewModel(application: Application): AndroidViewModel(application) {

val gameState = MutableLiveData<GameStateSuffix>()

    private var state = State(0)

    private val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score


    fun initGame() {
        viewModelScope.launch {
            val spellingRootList = spellingSuffix.toList()
            var randomWord = spellingRootList.random().replace("!", "")

            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)

            while (state.answers.any { it.first == randomWord } || !doesWordMeetCriteria) {
                randomWord = spellingRootList.random()

                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)
            }
            val modifiedWord = randomWord.replace(Regex("[А-Я]+"), "_")
            val uppercaseLetter = randomWord.filter { it.isUpperCase() }
            val uppercaseList = uppercaseLetter.windowed(1)
            val uppercaseShuffled = uppercaseList.shuffled()

            val rightAnswer = transformWord(randomWord)
            val answer = rightAnswer to ""
            val answers = state.answers.apply {
                add(answer)
            }

            allWordsDao.insertSmart(rightAnswer)
            state = state.copy(answers = answers)
            gameState.postValue(GameStateSuffix.NewWord(modifiedWord, uppercaseShuffled))

        }
    }

    fun handleWord(word: String, letter: String, button: Int){
        val modifiedWord = if (!word.contains("_"))
            word.replace(Regex("[А-Я]+"), "_").replace("_", letter)
        else {
            word.replace("_", letter)
        }
        gameState.value = GameStateSuffix.UpdateWord(
            modifiedWord,
            button
        )
    }

    fun   checkAnswer(word: String) {
        viewModelScope.launch {
            val updatedList = state.answers.apply {
                val last =
                    last().copy(second = word)
                set(lastIndex, last)
            }
            val lastAnswer = state.answers.last()
            val isAnswerRight = lastAnswer.first == lastAnswer.second
            val score = if (isAnswerRight) state.score + 1 else state.score

            allWordsDao.updateSmart(state.answers.last().first, isAnswerRight)

            state = state.copy(
                score = score,
                answers = updatedList
            )

            gameState.postValue(GameStateSuffix.CheckedAnswer(state))
        }
    }

    fun delay() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame()
            else
                gameState.value = GameStateSuffix.FinishGame(state)
        }
    }

    fun delete() {
        viewModelScope.launch {
            allWordsDao.deleteAll()
        }
    }
}