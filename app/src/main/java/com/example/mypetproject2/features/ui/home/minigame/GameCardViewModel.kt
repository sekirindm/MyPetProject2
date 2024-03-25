package com.example.mypetproject2.features.ui.home.minigame

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.baselist.multiList
import com.example.mypetproject2.data.baselist.spellingNN
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.features.ui.games.State
import com.example.mypetproject2.features.ui.games.spelling.spellingnn.GameState
import com.example.mypetproject2.utils.transformWord
import kotlinx.coroutines.launch

fun main() {
    var v = "esgfWKefgw"
//    val r = v.windowed(2, 2, true)
    val r = v.filter { it.isUpperCase() }.first()

    print(r)
}

sealed class GameStateCard() {
    data class NewWord(val word: String, val letters: List<String>) : GameStateCard()

    data class CheckedAnswer(val state: State) : GameStateCard()


    data class FinishGame(val state: State) : GameStateCard()

}

class GameCardViewModel(application: Application) : AndroidViewModel(application) {


    private val _updateScreen = MutableLiveData<List<HomeItemsList>>()
     val updateScreen: LiveData<List<HomeItemsList>> get() = _updateScreen

    var state = State(0)

    val gameState = MutableLiveData<GameStateCard>()
    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    //TODO Сделать так что бы при уничтожении fragment, слово не бралось заново
    fun initGame() {
        val list = multiList.random()

        val modifiedWord = list.replace(Regex("[А-Я]+"), "_")

        val upperLetters = list.first { it.isUpperCase() }

        val letters = list.filter { it.isUpperCase() }.windowed(1).shuffled()

        val lettersFilter = when(upperLetters) {
            'Н' -> listOf("Н", "НН")
            else -> {letters}
        }

        val rightAnswer = transformWord(list)
        val answer = rightAnswer to ""
        val answers = state.answers.apply {
            add(answer)
        }
        state = state.copy(answers = answers)
        Log.d("first", "$answer")

        gameState.postValue(GameStateCard.NewWord(modifiedWord, lettersFilter))
        Log.i("GameCardViewModel", " $modifiedWord, $letters")

    }

    fun checkWord(word: String) {
        val updatedList = state.answers.apply {
            val last = last().copy(second = word)
            set(lastIndex, last)
        }
        state = state.copy(answers = updatedList)

        gameState.postValue(GameStateCard.CheckedAnswer(state))
    }

    fun delay() {
        viewModelScope.launch {

            kotlinx.coroutines.delay(3000)
            if (state.answers.size < 3) {
                initGame()
            } else {
                gameState.value = GameStateCard.FinishGame(state)
            }

        }

    }

    fun updateScreen(word: String, letters: List<String>, rightAnswer: Boolean) {
        _updateScreen.value = listOf(
            HomeItemsList.HomeMiniGame(MiniGame(word, letters, rightAnswer))
        )
    }

}