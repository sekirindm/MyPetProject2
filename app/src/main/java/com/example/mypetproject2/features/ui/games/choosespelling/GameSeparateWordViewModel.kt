package com.example.mypetproject2.features.ui.games.choosespelling

import android.app.Application
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.separateList
import com.example.mypetproject2.features.ui.games.spelling.spellingpref.GameStatePref
import com.example.mypetproject2.features.ui.games.spelling.stringBuilder
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.State
import kotlinx.coroutines.launch

sealed class GameStateSeparate() {

    data class NewWord(val word: SpannableStringBuilder) : GameStateSeparate()

    data class CheckAnswer(val state: State) : GameStateSeparate()

    data class FinishGame(val state: State) : GameStateSeparate()
}

class GameSeparateWordViewModel(application: Application) : AndroidViewModel(application) {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score
    private var state = State(0)

    val gameState = MutableLiveData<GameStateSeparate>() // позволяет использовать лишь один MutableLiveData для всех состояний игры

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    fun initGame() {
        viewModelScope.launch {
            val spellingSeparateList = separateList.random()
            var randomWord = spellingSeparateList.first
            val rightIndex = spellingSeparateList.second
            val rightAnswer = when(rightIndex) {
                0 ->  randomWord.replace("(", "").replace(")", " ").replace("!", "")
                1 ->  randomWord.replace("(", "").replace(")", "").replace("!", "")
                else -> randomWord.replace("(", "").replace(")", "-").replace("!", "")
            }

            // TODO: проверять в дао райтансвер
            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(rightAnswer)

            while (state.answers.any { it.first == randomWord } || !doesWordMeetCriteria) {
                randomWord = spellingSeparateList.first
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(rightAnswer)
            }


             val answer = rightAnswer to ""
            val answers = state.answers.apply {
                add(answer)
            }
            val builderWord = stringBuilder(randomWord)

            allWordsDao.insertSmart(rightAnswer)
            state = state.copy(answers = answers)

            gameState.postValue(GameStateSeparate.NewWord(builderWord))

        }


    }
fun checkAnswer(word: String, buttonIndex: Int) {
    viewModelScope.launch {
        val answer = when(buttonIndex) {
            0 ->  word.replace("(", "").replace(")", " ")
            1 ->  word.replace("(", "").replace(")", "")
            else -> word.replace("(", "").replace(")", "-")
        }
        val updatedList = state.answers.apply {
            val last = last().copy(second = answer)
            set(
                lastIndex,
                last
            )
        }

        val lastAnswer = state.answers.last()
        val isAnswerRight = lastAnswer.first == lastAnswer.second
        val score = if (isAnswerRight) state.score + 1 else state.score
        allWordsDao.updateSmart(state.answers.last().first, isAnswerRight)

        state = state.copy(
            score = score,
            answers = updatedList
        )

        gameState.postValue(GameStateSeparate.CheckAnswer(state))
    }


}

    fun delay() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame()
            else
                gameState.value = GameStateSeparate.FinishGame(state)
        }
    }

    fun delete() {
        viewModelScope.launch {
            allWordsDao.deleteAll()
        }
    }

}