package com.example.mypetproject2.features.ui.games.punctuationgames

import android.app.Application
import android.text.Html
import android.text.SpannableStringBuilder
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.listPunctuationGameTwo
import com.example.mypetproject2.features.ui.games.stress.State
import kotlinx.coroutines.launch

sealed class GameStatePunctuation() {
    data class NewWord(val word: SpannableStringBuilder) : GameStatePunctuation()
    data class CheckWord(val state: State) : GameStatePunctuation()
    data class FinishGame(val state: State) : GameStatePunctuation()
}

class GamePunctuationViewModel(private val application: Application) : AndroidViewModel(application) {

    val state = State(0)

    val gameState = MutableLiveData<GameStatePunctuation>()

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    private var _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private fun initGame() {
        viewModelScope.launch {
            var list = listPunctuationGameTwo.random()
            val htmlText = "<sub>▢</sub>"
            val modifiedListToUnicode = list.replace(",", htmlText)
            val unicode = Html.fromHtml(modifiedListToUnicode, Html.FROM_HTML_MODE_LEGACY)
//            val span = spannableStringBuilderUnicode(unicode, application)
            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(list)

            while (state.answers.any{it.first == list} || !doesWordMeetCriteria) {
                list = listPunctuationGameTwo.random()
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(list)
            }


        }
    }
}