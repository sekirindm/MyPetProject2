package com.example.mypetproject2.features.ui.games.stress

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.database.gamedb.GameItemDb
import com.example.mypetproject2.data.database.allwordsdb.AllWordsDb
import kotlinx.coroutines.*
/**
 *
 * Sealed class (изолированный класс) — это класс, который является абстрактным и используется в Kotlin для ограничения классов, которые могут наследоваться от него.
 * Основная идея заключается в том, что sealed class позволяет определить ограниченный и известный заранее набор подклассов, которые могут быть использованы.
 *
 * Удобно использовать в перечислениях (when)
 * */


class GamesViewModel(application: Application) : AndroidViewModel(application) {


    private val gameItemDao = AppDatabase.getInstance(application).gameItemDao()

    private val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    private val _gameItemsLiveData = MutableLiveData<List<GameItemDb>>()
    private val gameItemsLiveData: LiveData<List<GameItemDb>> get() = _gameItemsLiveData

    private val _selectedVowelIndex = MutableLiveData(0)
    val selectedVowelIndex: LiveData<Int> get() = _selectedVowelIndex

    private val _selectedVowelChar = MutableLiveData<Char?>(null)
    val selectedVowelChar: LiveData<Char?> get() = _selectedVowelChar

    private val _userAnswersHistory = MutableLiveData<MutableList<String>>(mutableListOf())
    val userAnswersHistory: LiveData<MutableList<String>> = _userAnswersHistory

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score

    private val _userAnswers = MutableLiveData<MutableList<Boolean>>(mutableListOf())
    val userAnswers: LiveData<MutableList<Boolean>> get() = _userAnswers

    private val _totalAttempts = MutableLiveData(0)
    val totalAttempts: LiveData<Int> get() = _totalAttempts

    val wordCountLiveData = MutableLiveData<Int>()


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

    fun isWordAdded(word: String): Boolean = runBlocking {
        val gameItems = gameItemDao.getAllGameItems()
        gameItems.any { it.rightAnswer == word }
    }

    fun insertWord(word: String) {
        viewModelScope.launch {
            val currentItems = gameItemsLiveData.value ?: emptyList()
            val newPosition = currentItems.size
            val gameItem = GameItemDb(rightAnswer = word, position = newPosition)
            launch {
                gameItemDao.insert(gameItem)
            }
        }
    }

    fun deleteItem(word: String) {
        viewModelScope.launch {
            val gameItemToDelete = gameItemDao.getAllGameItems().find { it.rightAnswer == word }
            gameItemToDelete?.let {
                gameItemDao.delete(it)
            }
        }
    }


    fun insertWordToAllWords(word: String, count: Int) {
        viewModelScope.launch {
            val allWords = allWordsDao.getAllWords()
            val existingWord = allWords.find { it.words == word }

            if (existingWord != null) {
                existingWord.count = count
            } else {
                val newWord = AllWordsDb(words = word, count = count)
                allWordsDao.insert(newWord)
            }
        }
    }

    fun getWordCount(word: String) {
        viewModelScope.launch {
            val allWords = allWordsDao.getAllWords()
            val wordItem = allWords.find { it.words == word }
            val count = wordItem?.count ?: 0
            wordCountLiveData.value = count
        }
    }

    val gameItem: LiveData<List<GameItemDb>> = gameItemDao.getAllGameItems2()

    /**
     * Если из viewModelScope запускать suspend функцию, которая блокирует поток, то это заблокирует Main поток.
     * */

    fun updateRecyclerViewData() {
        viewModelScope.launch {
            val updatedData = gameItemDao.getAllGameItems()
            _gameItemsLiveData.value = updatedData
        }
    }


    fun deleteGameItem(item: GameItemDb) {
        viewModelScope.launch {
            gameItemDao.delete(item)
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

    companion object {
        const val MAX_ATTEMPTS = 5
    }
}