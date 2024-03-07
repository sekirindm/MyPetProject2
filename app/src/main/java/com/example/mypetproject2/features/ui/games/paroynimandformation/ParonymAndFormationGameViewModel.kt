package com.example.mypetproject2.features.ui.games.paroynimandformation

import android.app.Application
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.chaos.view.PinView
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.features.ui.games.State
import com.example.mypetproject2.utils.stringBuilder
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import kotlinx.coroutines.launch

sealed class GameStateParonym() {
    data class NewWord(val word: SpannableStringBuilder) : GameStateParonym()

    data class CheckWord(val state: State) : GameStateParonym()
    data class FinishGame(val state: State) : GameStateParonym()
}

class GameParonymViewModel(application: Application) : AndroidViewModel(application) {


    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score
    private var state = State(0)

    val gameState = MutableLiveData<GameStateParonym>()

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()
    fun updateScore(isCorrect: Boolean) {
        _score.value = _score.value?.let { score ->
            if (isCorrect) score + 1 else score
        }
        Log.d("updateScore", "_score.value ${_score.value} $isCorrect")
    }

    fun initGame(list: List<Pair<String, String>>) {
        viewModelScope.launch {
            val listPair = list.random()
            var randomWord = listPair.first
            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)

            while (state.answers.any { it.first == randomWord } || !doesWordMeetCriteria) {
                randomWord = listPair.first
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)
            }

            val answer = randomWord to " "
            val answers = state.answers.apply {
                add(answer)
            }
            state = state.copy(answers = answers)

            allWordsDao.insertSmart(randomWord.replace("!", ""))

            state.answers.apply {
                val last = last().copy(second = listPair.second)
                set(lastIndex, last)
            }

            val modifiedWord = stringBuilder(randomWord)
            gameState.postValue(GameStateParonym.NewWord(modifiedWord))
        }
    }


    fun checkAnswer(pvParonym: PinView) {
        viewModelScope.launch {
            val updateList = state.answers.apply {
                val last = last().copy(
                    second = last().second,
                    first = pvParonym.text.toString()
                ) // тут нужна та же пара, что и в initGame()
                Log.d("last().second", last.first)
                set(lastIndex, last)
            }
            val lastAnswer = state.answers.last()
            val rightAnswer = lastAnswer.second == pvParonym.text.toString()
            Log.d("lastAnswer.first", lastAnswer.first)
            val score = if (rightAnswer) state.score + 1 else state.score
            allWordsDao.updateSmart(lastAnswer.first.replace("!", ""), rightAnswer)
            state = state.copy(
                score = score,
                answers = updateList
            )
            gameState.postValue(GameStateParonym.CheckWord(state))
        }

    }

    fun delay(list: List<Pair<String, String>>) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame(list)
            else
                gameState.value = GameStateParonym.FinishGame(state)
        }
    }
}