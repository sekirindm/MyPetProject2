package com.example.mypetproject2.features.ui.games

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mypetproject2.features.ui.games.GamesFragment.Companion.MAX_ATTEMPTS

class GamesViewModel : ViewModel() {

    private val _shouldShowGameResults = MutableLiveData<Boolean>()
    val shouldShowGameResults: LiveData<Boolean> get() = _shouldShowGameResults

    private val _selectedVowelIndex = MutableLiveData<Int>()
    val selectedVowelIndex: LiveData<Int> get() = _selectedVowelIndex

    private val _selectedVowelChar = MutableLiveData<Char?>()
    val selectedVowelChar: LiveData<Char?> get() = _selectedVowelChar

    private val _vowelIndices = MutableLiveData<List<Int>>()
    val vowelIndices: LiveData<List<Int>> get() = _vowelIndices

    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int> get() = _score
    private val _userAnswers: MutableLiveData<MutableList<Boolean>> = MutableLiveData()
    val userAnswers: LiveData<MutableList<Boolean>> get() = _userAnswers

    private val _totalAttempts = MutableLiveData<Int>(0)
    val totalAttempts: LiveData<Int> get() = _totalAttempts

    var previousVowelIndex: Int = -1


    init {
        _selectedVowelIndex.value = -1
        _vowelIndices.value = mutableListOf()
        _selectedVowelChar.value = null
        _score.value = 0
        _totalAttempts.value = 0
        _userAnswers.value = mutableListOf()

    }

    fun setSelectedVowelIndex(index: Int) {
        _selectedVowelIndex.value = index
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
        if (currentAttempts >= MAX_ATTEMPTS) {
            _shouldShowGameResults.value = true
        } else {
            _totalAttempts.value = currentAttempts + 1
        }
    }
}