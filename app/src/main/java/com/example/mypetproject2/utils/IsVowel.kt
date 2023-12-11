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
//fun main() {
////    val list = separateList.shuffled()[0]
////    val options = list.toList()
////    print("${list.second}, ${list.first}")
//
////    val correctDisplay = separateList.find {it.first.replace("(", "").replace(")", "").trim() == rightAnswer.trim()}?.second
//
////    val kal = separateList.map {
////        val word = it.first
////        when (it.second) {
////            0 -> word.replace("(", "").replace(")", " ")
////            1 -> word.replace("(", "").replace(")", "")
////            2 -> word.replace("(", "").replace(")", "-")
////            else -> word
////        }
////    }
//
//    val w = stress[0].indexOfFirst { it.isUpperCase() }
//    print(w)
////
////    kal.forEach { println(it) }
//
////    val word = "!(НЕ)ВЕРЮ! в то что ты ушел"
////    val startIndex = word.indexOf("!")
////    val endIndex = word.indexOf("!", startIndex + 1)
////    val extractedText = if (startIndex != -1 && endIndex != -1) {
////        word.substring(startIndex, endIndex + 1)
////    } else {
////        "Выделенное слово не найдено"
////    }
////    print(extractedText.replace("!", "").lowercase())
//}
fun main() {
//    for(wordIndex in spellingPref.indices) {
//        var words = spellingPref[wordIndex].replace("!", "")
//        words = transformWord(words).lowercase()
//        val markerWord = words
//
//        println("\"$markerWord\" to \"\",")
    val spellingSeparateList = separateList
    var randomWord = spellingSeparateList.random()
    print(randomWord)

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
            if (answer.replace(Regex("[А-Я@]")) { "" } == it.replace(Regex("[А-Я@]")) { "" })
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

fun getPairChooseWord(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        listTriple.forEach {
            if (answer == it.first || answer == it.second || answer == it.third) {
                map.add(Pair(transformWordSuf(it.first), answer))
            }
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairChooseSeparateWord(userAnswers: List<String>): List<Pair<String, String>> {

    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        separateList.forEach {

            val formattedFirst = when (it.second) {
                0 -> it.first.replace("(", "").replace(")", " ")
                1 -> it.first.replace("(", "").replace(")", "")
                else -> it.first.replace("(", "").replace(")", "-")
            }
            if (answer.replace(" ", "").replace("-", "") == formattedFirst.replace(" ", "")
                    .replace("-", "")
            ) {
                map.add(Pair(transformWordSuf(formattedFirst), answer))
            }
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairParonym(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        paronymList.forEach {
            if (answer.replace(Regex("[А-Я!]")) { "" } == it.first.replace(Regex("[А-Я!]")) { "" }) {
                map.add(Pair(it.first, answer))
            }
        }
    }
    Log.d("getPair", "map $map")
    return map
}


fun getPair(userAnswers: List<String>): List<Pair<String, String>> {

    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        stress.forEach {
            if (answer.lowercase() == it.lowercase()) map.add(Pair(it, answer))
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
fun createSpannableStringBuilder(
    word: String,
    function: (Int, Char) -> Unit
): SpannableStringBuilder {
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



