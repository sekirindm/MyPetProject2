package com.example.mypetproject2.features.ui.home.minigame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.baselist.multiList
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.features.ui.games.State
import kotlinx.coroutines.launch

sealed class GameStateCard() {
    data class NewWord(val word: String, val letters: List<String>) : GameStateCard()

    data class CheckedAnswer(val state: State) : GameStateCard()

    data class FinishGame(val state: State) : GameStateCard()
}

class GameCardViewModel(application: Application) : AndroidViewModel(application) {

    val state = State(0)

    val gameState = MutableLiveData<GameStateCard>()
    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    fun initGame() {
        val list = multiList.random()

        val modifiedWord = list.replace(Regex("[А-Я]+"), "_")
        val letters = list.filter { it.isUpperCase() }.windowed(1).shuffled()

        gameState.postValue(GameStateCard.NewWord(modifiedWord, letters))
        Log.i("GameCardViewModel", " $modifiedWord, $letters")

    }

    fun delay() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < 3)
                initGame()
            else
                gameState.value = GameStateCard.FinishGame(state)
        }
    }

}