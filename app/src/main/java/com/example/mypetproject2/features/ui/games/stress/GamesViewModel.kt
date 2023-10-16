package com.example.mypetproject2.features.ui.games.stress

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.database.GameItemDb
import com.example.mypetproject2.data.database.allwordsdb.AllWordsDb
import com.example.mypetproject2.data.spellingNN
import kotlinx.coroutines.*

sealed class GameState {
    data class NewWord(val word: String) : GameState()

    data class UpdateWord(val word: String, val button: Int) : GameState()

    data class CheckedAnswer(val isCorrect: Boolean) : GameState()
}

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    val gameState = MutableLiveData<GameState>()

    private val gameItemDao = AppDatabase.getInstance(application).gameItemDao()

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()

    private val _gameItemsLiveData = MutableLiveData<List<GameItemDb>>()
    val gameItemsLiveData: LiveData<List<GameItemDb>> get() = _gameItemsLiveData

    private val _allItemsLiveData = MutableLiveData<List<AllWordsDb>>()
    val allItemsLiveData: LiveData<List<AllWordsDb>> get() = _allItemsLiveData

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

    private val _isWordAddedLiveData = MutableLiveData<Boolean>()
    val isWordAddedLiveData: LiveData<Boolean> get() = _isWordAddedLiveData

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

//    fun checkWord(word: String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val isWordValid = allWordsDao.isWordValid(word)
//            Log.d("checkWord", "isWordValid $isWordValid")
//        }
//    }

    fun isWordAdded(word: String): Boolean = runBlocking {
        val gameItems = gameItemDao.getAllGameItems()
        gameItems.any { it.rightAnswer == word }
    }

    fun isWordInDatabase(word: String): LiveData<Boolean> {
        return gameItemDao.isWordInDatabase(word)
    }

    fun insertWord(word: String) {
        viewModelScope.launch {
            val currentItems = gameItemsLiveData.value ?: emptyList()
            val newPosition = currentItems.size
            val gameItem = GameItemDb(rightAnswer = word, position = newPosition)

            withContext(Dispatchers.IO) {
                gameItemDao.insert(gameItem)
            }
        }
    }

    fun deleteItem(word: String) {
        CoroutineScope(Dispatchers.IO).launch {
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
                allWordsDao.updateWordCount(existingWord)
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


//    fun getWordCount(word: String) {
//        viewModelScope.launch {
//            val allWords = allWordsDao.getAllWords()
//            val wordItem = allWords.find { it.word == word }
//            wordCountLiveData.postValue(wordItem?.count)
//        }
//    }


    fun updateWordCount(word: String, countDelta: Int) {
        viewModelScope.launch {
            val currentCount = allWordsDao.getWordCount(word) ?: 0
            allWordsDao.updateWordCount(word, currentCount + countDelta)
        }
    }


    val gameItem: LiveData<List<GameItemDb>> = gameItemDao.getAllGameItems2()

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

    fun deleteAndQueryAllItems(item: GameItemDb) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                gameItemDao.delete(item)
            }
            updateRecyclerViewData()
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

    fun initGame() {
        val spellingNNList = spellingNN.toList()
        val randomWord = spellingNNList.random()
        val modifiedWord = randomWord.replace("Н+".toRegex(), "_")

        gameState.value = GameState.NewWord(modifiedWord)
    }

    fun handleWord(word: String, letter: String, button: Int) {

        val modifiedWord = if (!word.contains("_"))
            word.replace("Н+".toRegex(), "_")
                .replace("_", letter)
        else
            word.replace("_", letter)

        gameState.value = GameState.UpdateWord(modifiedWord, button)
    }

    fun checkAnswer(word: String) {
        gameState.value = GameState.CheckedAnswer(spellingNN.contains(word))
    }

    fun delay() {
        viewModelScope.launch {
            delay(1000)
            initGame()
        }
    }
}