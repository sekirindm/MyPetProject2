package com.example.mypetproject2.features.ui.games

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypetproject2.features.ui.games.GamesFragment.Companion.MAX_ATTEMPTS

class GamesViewModel : ViewModel() {

    private val _selectedVowelIndex = MutableLiveData<Int>(-1)
    val selectedVowelIndex: LiveData<Int> get() = _selectedVowelIndex

    private val _selectedVowelChar = MutableLiveData<Char?>(null)
    val selectedVowelChar: LiveData<Char?> get() = _selectedVowelChar

    private val _userAnswersHistory = MutableLiveData<MutableList<String>>(mutableListOf())
    val userAnswersHistory: LiveData<MutableList<String>> = _userAnswersHistory

    private val _score = MutableLiveData<Int>(0)
    val score: LiveData<Int> get() = _score

    private val _userAnswers = MutableLiveData<MutableList<Boolean>>(mutableListOf())
    val userAnswers: LiveData<MutableList<Boolean>> get() = _userAnswers

    private val _totalAttempts = MutableLiveData<Int>(0)
    val totalAttempts: LiveData<Int> get() = _totalAttempts

    fun setSelectedVowelIndex(index: Int) {
        _selectedVowelIndex.value = index
    }

    fun setUserAnswers(answersHistory: String) {
        val updatedList = _userAnswersHistory.value ?: mutableListOf()
        updatedList.add(answersHistory)
        _userAnswersHistory.value = updatedList
    }

    fun setSelectedVowelChar(char: Char?) {
        _selectedVowelChar.value = char
    }

    fun updateScore(isCorrect: Boolean) {
        _score.value = _score.value?.let { score ->
            if (isCorrect) score + 1 else score
        }
    }

    fun addUserAnswer(answer: Boolean) {
        val updatedList = _userAnswers.value ?: mutableListOf()
        updatedList.add(answer)
        _userAnswers.value = updatedList
    }

    fun incrementTotalAttempts() {
        val currentAttempts = _totalAttempts.value ?: 0
        _totalAttempts.value = currentAttempts + 1
    }
}