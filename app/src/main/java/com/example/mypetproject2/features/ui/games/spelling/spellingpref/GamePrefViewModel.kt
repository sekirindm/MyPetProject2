package com.example.mypetproject2.features.ui.games.spelling.spellingpref

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.spellingNN
import com.example.mypetproject2.data.spellingPref
import com.example.mypetproject2.features.ui.games.stress.GameState
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.State
import kotlinx.coroutines.launch
sealed class GameStatePref {
    data class NewWord(val word: String, val letters: List<String>) : GameStatePref()

    data class UpdateWord(val word: String, val button: Int) : GameStatePref()

    data class CheckedAnswer(val state: State) : GameStatePref()

    data class FinishGame(val state: State) : GameStatePref()
}
fun main() {
    var wordIndex = 0
    val modifiedWord = spellingPref.toList()[wordIndex].replace(Regex("[А-Я]+")) {"_"}
    print(modifiedWord)
}
class GamePrefViewModel(application: Application): AndroidViewModel(application) {

    private val allWordDao = AppDatabase.getInstance(application).allWordsDao()

    private var state = State(0)
    val gameState = MutableLiveData<GameStatePref>()
    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    fun initGame() {
        viewModelScope.launch {
            val spellingPrefList = spellingPref.toList().take(7)
            var randomWord = spellingPrefList.random()

            var doesWordMeetCriteria = allWordDao.doesWordMeetCriteria(randomWord)
            while (state.answers.any {it.first == randomWord } || !doesWordMeetCriteria) {
                 randomWord = spellingPrefList.random()
                 doesWordMeetCriteria = allWordDao.doesWordMeetCriteria(randomWord)
            }

            val modifiedWord = randomWord.replace(Regex("[А-Я]+"), "_")
            val uppercaseLetter = randomWord.filter {it.isUpperCase()}
            val uppercaseList = uppercaseLetter.windowed(1)
            val uppercaseShuffled = uppercaseList.shuffled()

            val answer = randomWord to ""
            val answers = state.answers.apply {
                add(answer)
            }

            allWordDao.insertSmart(randomWord)
            state = state.copy(answers = answers)
            gameState.postValue(GameStatePref.NewWord(modifiedWord, uppercaseShuffled))
        }
    }

    fun handleWord(word: String, letter: String, button: Int){
       val modifiedWord = if (!word.contains("_"))
            word.replace(Regex("[А-Я]+"), "_").replace("_", letter)
        else {
            word.replace("_", letter)
        }
        gameState.value = GameStatePref.UpdateWord(
            modifiedWord,
            button
        )
    }

    fun checkAnswer(word: String) {
        viewModelScope.launch {
            val updatedList = state.answers.apply {
                val last =
                    last().copy(second = word)
                set(
                    lastIndex,
                    last
                )
            }
            val isAnswerRight = spellingNN.contains(word)
            val score = if (isAnswerRight) state.score + 1 else state.score

            allWordDao.updateSmart(state.answers.last().first, isAnswerRight)

            state = state.copy(
                score = score,
                answers = updatedList
            )

            gameState.postValue(GameStatePref.CheckedAnswer(state))
        }
    }

    fun delay() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame()
            else
                gameState.value = GameStatePref.FinishGame(state)
        }
    }

    fun delete() {
        viewModelScope.launch {
            allWordDao.deleteAll()
        }
    }

}