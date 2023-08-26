package com.example.mypetproject2.features.ui.games.stress

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.database.GameItemDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.FieldPosition

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private val gameItemDao = AppDatabase.getInstance(application).gameItemDao()

    private val _gameItemsLiveData = MutableLiveData<List<GameItemDb>>()
    val gameItemsLiveData: LiveData<List<GameItemDb>> get() = _gameItemsLiveData

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


//    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()

    fun setSelectedVowelIndex(index: Int) {
        _selectedVowelIndex.value = index
    }

    fun setUserAnswers(answersHistory: String) {
        val updatedList = _userAnswersHistory.value ?: mutableListOf()
        updatedList.add(answersHistory)
        _userAnswersHistory.value = updatedList
        Log.d("setUserAnswers", "updatedList $updatedList")
    }

    fun setSelectedVowelChar(char: Char?) {
        _selectedVowelChar.value = char
    }

    suspend fun isWordAdded(word: String): Boolean {
        val gameItems = gameItemDao.getAllGameItems()
        return gameItems.any { it.rightAnswer == word }
    }

    suspend fun removeWord(word: String) {
        withContext(Dispatchers.IO) {
            val gameItemToDelete = gameItemDao.getAllGameItems().find { it.rightAnswer == word }
            gameItemToDelete?.let {
                gameItemDao.delete(it)
            }
        }
    }

    fun toggleWord(word: String) {
        viewModelScope.launch {
            if (isWordAdded(word)) {
                removeWord(word)
            } else {
                insertWord(word)
            }
        }
    }

    fun insertWord(word: String) {
        viewModelScope.launch {
            val currentItems = gameItemsLiveData.value ?: emptyList()
            val newPosition = currentItems.size
            val gameItem = GameItemDb(rightAnswer = word, position = newPosition)

            withContext(Dispatchers.IO) {
                gameItemDao.insert(gameItem)
            }

            queryAllItems()
        }
    }

    fun updateIconAndDatabase(word: String) {
        viewModelScope.launch {
            if (isWordAdded(word)) {
                removeWord(word)
            } else {
                insertWord(word)
            }
        }
    }

    suspend fun deleteGameItem(item: GameItemDb) {
        withContext(Dispatchers.IO) {
            gameItemDao.delete(item)
        }
    }
    fun deleteAndQueryAllItems(item: GameItemDb) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gameItemDao.delete(item)
            }
            queryAllItems()
        }
    }

     fun queryAllItems() {
        viewModelScope.launch {
            val gameItems = gameItemDao.getAllGameItems()
            _gameItemsLiveData.postValue(gameItems)
        }
    }


    fun updateScore(isCorrect: Boolean) {
        _score.value = _score.value?.let { score ->
            if (isCorrect) score + 1 else score
        }
        Log.d("updateScore", "_score.value ${_score.value} $isCorrect")
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