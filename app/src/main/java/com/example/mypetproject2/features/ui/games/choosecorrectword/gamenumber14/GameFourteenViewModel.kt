package com.example.mypetproject2.features.ui.games.choosecorrectword.gamenumber14

import android.app.Application
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.baselist.fourteenList
import com.example.mypetproject2.data.baselist.separateList
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.features.ui.games.State
import com.example.mypetproject2.features.ui.games.choosecorrectword.gamenumber13.GameStateSeparate
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.utils.stringBuilder
import kotlinx.coroutines.launch

sealed class GameStateFourteen() {
    data class NewWord(val word: SpannableStringBuilder) : GameStateFourteen()
    data class CheckAnswer(val state: State) : GameStateFourteen()
    data class FinishGame(val state: State) : GameStateFourteen()
}
class GameFourteenViewModel(application: Application) : AndroidViewModel(application) {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    fun updateScore(isCorrect: Boolean) {
        _score.value = _score.value?.let { score ->
            if (isCorrect) score + 1 else score
        }
        Log.d("updateScore", "_score.value ${_score.value} $isCorrect")
    }

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    var state = State(0)

    val gameState = MutableLiveData<GameStateFourteen>()
    fun initGame() {
        viewModelScope.launch {
            val fourteenList = fourteenList.random()
            var randomWord = fourteenList.first
            val rightAnswer = when(fourteenList.second) {
                0 ->  randomWord.replace("(", "").replace(")", " ").replace("!", "")
                1 ->  randomWord.replace("(", "").replace(")", "").replace("!", "")
                else -> randomWord.replace("(", "").replace(")", "-").replace("!", "")
            }

            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(rightAnswer)

            while (state.answers.any { it.first == randomWord } || !doesWordMeetCriteria) {
                randomWord = fourteenList.first
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(rightAnswer)
            }

            val answer = rightAnswer to ""
            val answers = state.answers.apply {
                add(answer)
            }
            val builderWord = stringBuilder(randomWord)

            allWordsDao.insertSmart(rightAnswer)
            state = state.copy(answers = answers)

            gameState.postValue(GameStateFourteen.NewWord(builderWord))
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
            gameState.postValue(GameStateFourteen.CheckAnswer(state))
        }
    }

    fun delay() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame()
            else
                gameState.value = GameStateFourteen.FinishGame(state)
        }
    }
}