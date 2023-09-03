package com.example.mypetproject2.features

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import com.example.mypetproject2.data.*
import com.example.mypetproject2.features.ui.games.Rules
import com.example.mypetproject2.features.ui.games.spelling.transformWord
import com.example.mypetproject2.features.ui.games.spelling.transformWordSuf
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import java.util.*


fun isVowel(c: Char): Boolean {
    val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
    return c.lowercaseChar() in vowels
}


/**
 * нам нужен метод gatPairSpelling который будет сравнивать слово из списка с сответом пользователя.
 * 1. Ответ пользователя нужно передавать в параметры(List<String>).
 * 2. Возвращает List<Pair<String, String>>, где первое слово является словом из списка, а второе - ответом пользователя.
 * 3. для каждого ответа пользователя С помощью цикла forEach, ищем соответсвующее слово из списка
 * 3.1
 *
 * */
fun markString(string: String): String {
    val markedString = "!$string!"
    return markedString
}
fun main() {
//    for(wordIndex in spellingPref.indices) {
//        var words = spellingPref[wordIndex].replace("!", "")
//        words = transformWord(words).lowercase()
//        val markerWord = words
//
//        println("\"$markerWord\" to \"\",")


    }



//    val correct = "усид!чИЕв!ый"
//
//    val rules = mapOf(
//        "чИЕв" to "В суффиксах -ЛИВ- и -ЧИВ- (производных от -ИВ-) пишется буква и: заботлИВый, заносчИВый. Следует различать прилагательные с суффиксами -ЕВ-, с одной стороны, и -ИВ-, -ЛИВ-, -ЧИВ с другой. Слова на -ЕВый — напр., соЕВый, ферзЕВый, фланелЕВый, матчЕВый, замшЕВый, — содержат суффикс -ЕВ- (орфографическая разновидность суффикса -ОВ-), проверяющийся под ударением в таких словах, как дубо́вый, рублёвый.\n "
//    )
//
//    val suffixStart = correct.indexOf('!') + 1
//    val suffixEnd = correct.lastIndexOf('!')
//    val suffix = correct.substring(suffixStart, suffixEnd)
//
//    val rule = rules[suffix]
//
//        println(rule)



//    val userAnswers = "прИнеприятная"
//    val modified = userAnswers.filter { it.isLowerCase() }
//    println(userAnswers.filter { it.isLowerCase() } == correct.filter { it.isLowerCase() })

//    println("${userAnswers.replace("ЕИ", "") == correct.replace("ЕИ", "")}")

fun getPairSpelling(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answers ->
        spellingNN.forEach {
            if (answers.replace("Н", "") == it.replace("Н", "")) map.add(Pair(it, answers))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingPref(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingPref.forEach {
            if (answer.replace(Regex("[А-Я]")) { "" } == it.replace(Regex("[А-Я]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingRoot(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingRoot.forEach {
            if (answer.replace(Regex("[А-Я!]")) { "" } == it.replace(Regex("[А-Я!]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingSuffix(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingSuffix.forEach {
            if (answer.replace(Regex("[А-Я!]")) { "" } == it.replace(Regex("[А-Я!]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPair(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    stress.forEach { stress ->
        userAnswers.forEach { answer ->
            if (answer.lowercase() == stress.lowercase()) map.add(Pair(stress, answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun check(list: List<Pair<String, String>>) = list.map { it.first == it.second }

// createSpannableStringBuilder(word): Создает SpannableStringBuilder для заданного слова,
// где каждая гласная буква делается кликабельной с помощью ClickableSpan.
// createClickableSpan(characterIndex, character): Создает ClickableSpan для заданного индекса и символа гласной буквы.
// При клике на эту букву будет вызываться метод handleVowelClick(characterIndex, character).
 fun createSpannableStringBuilder(word: String, function: (Int, Char) -> Unit): SpannableStringBuilder {
    val builder = SpannableStringBuilder(word.lowercase(Locale.getDefault()))

    for (characterIndex in word.indices) {
        val character = word[characterIndex]
        if (isVowel(character)) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    function(characterIndex, character)
                }
            }
            builder.setSpan(
                clickableSpan,
                characterIndex,
                characterIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    return builder
}


fun createCustomResultSpannableStringBuilder(
    word: String,
    correctIndex: Boolean,
    selectedVowelIndex: Int?,
    selectedVowelChar: Char?
): SpannableStringBuilder {
    val builder = SpannableStringBuilder(word)
    for (i in word.indices) {
        val currentChar = word[i]

        if (currentChar.isUpperCase()) {
            val color = if (correctIndex) Color.GREEN else Color.RED

            if (i == selectedVowelIndex && selectedVowelChar != null) {
                if (currentChar.equals(selectedVowelChar, ignoreCase = true)) {
                    builder.setSpan(
                        ForegroundColorSpan(Color.GREEN),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    builder.setSpan(
                        ForegroundColorSpan(Color.RED),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                }
            } else {
                builder.setSpan(
                    ForegroundColorSpan(color),
                    i,
                    i + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
    }

    return builder
}

// suspend fun getRandomWord(): String? {
//    val maxAttempts = 5 // Максимальное количество попыток получить слово
//    var currentAttempt = 0
//    var randomWord: String?
//
//    do {
//        randomWord = getRandomWordFromDatabase()
//        currentAttempt++
//    } while (randomWord != null && currentAttempt < maxAttempts)
//
//    return randomWord
//}
//
// suspend fun getRandomWordFromDatabase(viewModel: GamesViewModel, word: String): String? {
//    val wordCount = viewModel.getWordCount(word)
//    return if (wordCount == null || wordCount <= 5) {
//        // Получаем слово из базы данных, у которого счетчик меньше или равен 5
//        val wordList = viewModel.getAllItems()
//        val filteredWords = wordList.filter { it.count <= 5 }
//        if (filteredWords.isNotEmpty()) {
//            filteredWords[Random().nextInt(filteredWords.size)].words
//        } else {
//            null // Если не осталось слов с счетчиком <= 5, вернуть null
//        }
//    } else {
//        null // Если счетчик больше 5, вернуть null
//    }
//}



