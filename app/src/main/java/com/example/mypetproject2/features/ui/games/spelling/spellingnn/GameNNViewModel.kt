  package com.example.mypetproject2.features.ui.games.spelling.spellingnn

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mypetproject2.data.database.AppDatabase
import com.example.mypetproject2.data.baselist.spellingNN
import com.example.mypetproject2.features.ui.games.State
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import kotlinx.coroutines.launch

sealed class GameState {
    data class NewWord(val word: String) : GameState()

    data class UpdateWord(val word: String, val button: Int) : GameState()

    data class CheckedAnswer(val state: State) : GameState()

    data class FinishGame(val state: State) : GameState()
}

class GameNNViewModel(application: Application): AndroidViewModel(application) {

    private val _score = MutableLiveData(0)
    val score: LiveData<Int> get() = _score
    private var state = State(0)

    val gameState = MutableLiveData<GameState>() // позволяет использовать лишь один MutableLiveData для всех состояний игры

    val allWordsDao = AppDatabase.getInstance(application).allWordsDao()
    fun updateScore(isCorrect: Boolean) {
        _score.value = _score.value?.let { score ->
            if (isCorrect) score + 1 else score
        }
        Log.d("updateScore", "_score.value ${_score.value} $isCorrect")
    }

    /**
     * Тут начинаем нашу игрулю
     * */
    fun initGame() {
        viewModelScope.launch {


            val spellingNNList = spellingNN.toList().take(5) // получаем список в виде листа
            var randomWord = spellingNNList.random() // берем рандомное слово

            var doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)
            Log.i("TAGG", "b $doesWordMeetCriteria")

            // TODO: может породить бесконечний цикл, если в списке не осталось подходящих слов
            while (state.answers.any { it.first == randomWord } || !doesWordMeetCriteria) { // проверяем, есть ли это слово в списе ответов (чтобы избежать повторений)

                Log.i("TAGG", "wrong $randomWord MeetCriteria $doesWordMeetCriteria")
                randomWord = spellingNNList.random()  // если оно там есть, берем новое слово до тех пор, пока оно не будет не быть в списке
                doesWordMeetCriteria = allWordsDao.doesWordMeetCriteria(randomWord)
                Log.i("TAGG", "doesWord $randomWord MeetCriteria $doesWordMeetCriteria")
            }

            Log.i("TAGG", "after")
            val modifiedWord = randomWord.replace(
                "Н+".toRegex(),
                "_"
            ) // заменяем Н или НН на _  (листвеННица -> листве_ица)
            val answer = randomWord to ""
            val answers = state.answers.apply {
                add(answer)
            }

            allWordsDao.insertSmart(randomWord)
            state =
                state.copy(answers = answers) // копируем состояние с заменой списка ответов на новый. Это позваляет всегда иметь актуальное состояние
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

        gameState.value = GameState.UpdateWord(
            modifiedWord,
            button
        ) // упаковываем это слово в объект UpdateWord, которое на фрагменте поможет понять на каком мы этапе
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
                val last =
                    last().copy(second = word) // добавляем в пару к изначальному слову ответ пользователя
                set(
                    lastIndex,
                    last
                ) // устанавливаем пару в список. Метод set заменяет элемент списка на переданный
            }
            val isAnswerRight = spellingNN.contains(word)
            val score = if (isAnswerRight) state.score + 1 else state.score

            allWordsDao.updateSmart(state.answers.last().first, isAnswerRight)

            state = state.copy(
                score = score,
                answers = updatedList
            ) // копируем состояние с заменой счета и списка ответов на новый. Это позваляет всегда иметь актуальное состояние

            gameState.postValue(GameState.CheckedAnswer(state))
        }
    }

    fun delay() {
        viewModelScope.launch {
            kotlinx.coroutines.delay(1000)
            if (state.answers.size < GamesViewModel.MAX_ATTEMPTS)
                initGame()
            else
                gameState.value = GameState.FinishGame(state)
        }
    }

    fun delete() {
        viewModelScope.launch {
            allWordsDao.deleteAll()
        }
    }

}