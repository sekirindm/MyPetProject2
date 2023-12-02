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

data class State(
    val score: Int,
    val answers: MutableList<Pair<String, String>> = mutableListOf()
)

/**
 *
 * Sealed class (изолированный класс) — это класс, который является абстрактным и используется в Kotlin для ограничения классов, которые могут наследоваться от него.
 * Основная идея заключается в том, что sealed class позволяет определить ограниченный и известный заранее набор подклассов, которые могут быть использованы.
 *
 * Удобно использовать в перечислениях (when)
 * */
sealed class GameState {
    data class NewWord(val word: String) : GameState()

    data class UpdateWord(val word: String, val button: Int) : GameState()

    data class CheckedAnswer(val state: State) : GameState()

    data class FinishGame(val state: State) : GameState()
}

class GamesViewModel(application: Application) : AndroidViewModel(application) {

    private var state = State(0)

    val gameState = MutableLiveData<GameState>() // позволяет использовать лишь один MutableLiveData для всех состояний игры

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
//                allWordsDao.updateWordCount(existingWord)
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
//        viewModelScope.launch {
//            val currentCount = allWordsDao.getWordCount(word)
//            currentCount.collect {
//                allWordsDao.updateWordCount(word, (it?:0) + countDelta)
//            }
//        }
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










    /**
     * Тут начинаем нашу игрулю
     * */
    fun initGame() {
        viewModelScope.launch {


            val spellingNNList = spellingNN.toList().take(7) // получаем список в виде листа
            var randomWord = spellingNNList.random() // берем рандомное слово

            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)
            Log.i("TAGG", "b $doesWordMeetCriteria")

            // TODO: может породить бесконечний цикл, если в списке не осталось подходящих слов
            while (state.answers.any { it.first ==  randomWord} || !doesWordMeetCriteria) { // проверяем, есть ли это слово в списе ответов (чтобы избежать повторений)

                Log.i("TAGG", "wrong $randomWord MeetCriteria $doesWordMeetCriteria")
                randomWord = spellingNNList.random()  // если оно там есть, берем новое слово до тех пор, пока оно не будет не быть в списке
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)
                Log.i("TAGG", "doesWord $randomWord MeetCriteria $doesWordMeetCriteria")
            }

            Log.i("TAGG", "after")
            val modifiedWord = randomWord.replace("Н+".toRegex(), "_") // заменяем Н или НН на _  (листвеННица -> листве_ица)
            val answer = randomWord to ""
            val answers = state.answers.apply {
                add(answer)
            }

            allWordsDao.insertSmart(randomWord)
            state = state.copy(answers = answers) // копируем состояние с заменой списка ответов на новый. Это позваляет всегда иметь актуальное состояние
            gameState.postValue(GameState.NewWord(modifiedWord))  // упаковываем это слово в объект NewWord, которое на фрагменте поможет понять на каком мы этапе
        }
    }


    /**
     * Сюда попадает слова после нажатия на любую букву.
     *
     * [letter] - Н или НН
     * */
    fun handleWord(word: String, letter: String, button: Int) {
        val modifiedWord = if (!word.contains("_")) // если в слове уже заменена _ на букву Н, то заменяем ее на _
            word.replace("Н+".toRegex(), "_")  // то заменяем ее на _, (листвеННица -> листве_ица)
                .replace("_", letter)             // а затем заменяем на [letter] (листве_ица -> листвеНица)
        else                                              // если это первая замена, т.е. _ есще в слове,
            word.replace("_", letter)             // то просто заменяем _ на [letter] (листве_ица -> листвеНица)

        gameState.value = GameState.UpdateWord(modifiedWord, button) // упаковываем это слово в объект UpdateWord, которое на фрагменте поможет понять на каком мы этапе
    }

    /**
     * ʕ•́ᴥ•̀ʔっ ʕ•́ᴥ•̀ʔっ ʕ•́ᴥ•̀ʔっ ʕ•́ᴥ•̀ʔっ ʕ•́ᴥ•̀ʔっ ʕ•́ᴥ•̀ʔっ
     *
     * Вот что должно происходить при проверке ответа :
     *
     * 1. проверяем, правильный ли ответ TRUE / FALSE
     * 2. обновляем счет +1 / +0
     * 3. обновить слово в бд
     * 4. добавить в список для экрана результатов (─‿‿─)
     *
     * */
    fun checkAnswer(word: String) {
        viewModelScope.launch {
            val updatedList = state.answers.apply { // обновляем список ответов
                val last = last().copy(second = word) // добавляем в пару к изначальному слову ответ пользователя
                set(lastIndex, last) // устанавливаем пару в список. Метод set заменяет элемент списка на переданный
            }
            val isAnswerRight = spellingNN.contains(word)
            val score = if (isAnswerRight) state.score + 1 else state.score

            allWordsDao.updateSmart(state.answers.last().first, isAnswerRight)

            state = state.copy(score = score, answers = updatedList) // копируем состояние с заменой счета и списка ответов на новый. Это позваляет всегда иметь актуальное состояние

            gameState.postValue(GameState.CheckedAnswer(state))
        }
    }

    fun delay() {
        viewModelScope.launch {
            delay(1000)
            if (state.answers.size < MAX_ATTEMPTS)
                initGame()
            else
                gameState.value = GameState.FinishGame(state)
        }
    }

    fun delete(){
        viewModelScope.launch {
            allWordsDao.deleteAll()
        }
    }

    companion object {
        const val MAX_ATTEMPTS = 5
    }
}