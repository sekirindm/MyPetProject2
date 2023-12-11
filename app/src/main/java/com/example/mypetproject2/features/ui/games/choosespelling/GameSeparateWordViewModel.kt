package com.example.mypetproject2.features.ui.games.choosespelling

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.separateList
import com.example.mypetproject2.features.ui.games.spelling.spellingnn.GameState
import com.example.mypetproject2.features.ui.games.stress.State

sealed class GameStateSeparate() {

    data class NewWord(val word:String, val letters: List<String>) : GameStateSeparate()

    data class CheckAnswer(val state: State) : GameStateSeparate()

    data class FinishGame(val state: State) : GameStateSeparate()
}
class GameSeparateWordViewModel(application: Application): AndroidViewModel(application) {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score
    private var state = State(0)

    val gameState = MutableLiveData<GameState>() // позволяет использовать лишь один MutableLiveData для всех состояний игры

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    fun initGame() {
        var wordIndex = 0
        val spellingSeparateList = separateList[wordIndex]
        var randomWord = spellingSeparateList.first


    }


}