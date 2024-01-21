package com.example.mypetproject2.features.ui.games.punctuationgames

import android.app.Application
import android.content.Context
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.listPunctuationGameTwo
import com.example.mypetproject2.features.spannableStringBuilderUnicode
import com.example.mypetproject2.features.ui.games.spelling.spellingpref.GameStatePref
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.State
import kotlinx.coroutines.launch

sealed class GameStatePunctuation() {
    data class NewWord(val word: SpannableStringBuilder) : GameStatePunctuation()
    data class CheckWord(val state: State) : GameStatePunctuation()
    data class FinishGame(val state: State) : GameStatePunctuation()
}

class GamePunctuationViewModel(private val application: Application) : AndroidViewModel(application) {

    var state = State(0)

    val gameState = MutableLiveData<GameStatePunctuation>()

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    private var _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    fun initGame(listGame: List<String>, context: Context, textView: TextView) {
        viewModelScope.launch {
            var list = listGame.random()
            val htmlText = "▢"
            val listForDoesWordMeetCriteria = list.replace("!", "")
            val modifiedListToUnicode = list.replace(",", htmlText).replace("!", htmlText)
//            val span = spannableStringBuilderUnicode(unicode, application)

            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(listForDoesWordMeetCriteria)

            while (state.answers.any { it.first == list } || !doesWordMeetCriteria) {
                list = listPunctuationGameTwo.random()
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(listForDoesWordMeetCriteria)
            }
            val answer = list to ""
            val answers = state.answers.apply {
                add(answer)
            }
            val modifiedToUnicode =
                spannableStringBuilderUnicode(modifiedListToUnicode, context, textView)
            allWordsDao.insertSmart(list)
            state = state.copy(answers = answers)
            gameState.postValue(GameStatePunctuation.NewWord(modifiedToUnicode))

        }
    }

    fun checkAnswer(word:String){
        viewModelScope.launch {
            val updateList = state.answers.apply {
                val last = last().copy(second = word)
                set(lastIndex, last)
            }
            val lastAnswer = state.answers.last()
            val isAnswerRight = lastAnswer.first.replace("!", "") == lastAnswer.second
            Log.d("isAnswerRightFirst", lastAnswer.first)
            Log.d("isAnswerRightSecornd", lastAnswer.second)
            val score = if (isAnswerRight) state.score + 1 else state.score
            allWordsDao.updateSmart(state.answers.last().first.replace("!", ""), isAnswerRight)
            state = state.copy(
                score = score,
                answers = updateList
            )
            gameState.postValue(GameStatePunctuation.CheckWord(state))
        }
    }

    fun delay(listGame: List<String>, context: Context, textView: TextView) {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame(listGame, context, textView)
            else
                gameState.value = GameStatePunctuation.FinishGame(state)
        }
    }

    fun delete() {
        viewModelScope.launch {
            allWordsDao.deleteAll()
        }
    }
}