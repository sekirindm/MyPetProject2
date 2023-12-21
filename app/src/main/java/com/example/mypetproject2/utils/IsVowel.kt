package com.example.mypetproject2.features

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mypetproject2.R
import com.example.mypetproject2.data.*
import com.example.mypetproject2.features.ui.games.spelling.transformWordSuf
import com.example.mypetproject2.features.ui.games.stress.logic.CustomClickableSpan
import com.example.mypetproject2.features.ui.games.stress.logic.NoUnderlineClickableSpan
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import android.widget.Toast as Toast1


fun isVowel(c: Char): Boolean {
    val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
    return c.lowercaseChar() in vowels
}
fun isSubUnicode(s: Spanned): Boolean {
    val htmlText = "<sub>▢</sub>"
    val unicode = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    return s in unicode

}

fun main() {
    CoroutineScope(Dispatchers.IO).launch {
        val list = listPunctuationGameTwo.random()
        val htmlText = "<sub>▢</sub>"
        val modifiedListToUnicode = list.replace(",", htmlText)
        val unicode = Html.fromHtml(modifiedListToUnicode, Html.FROM_HTML_MODE_LEGACY)
        isSubUnicode(unicode)
    }
}
@SuppressLint("ResourceType")
fun spannableStringBuilderUnicode(
    word: Spanned,
    context: Context
): SpannableStringBuilder {

    val htmlText = "<sub>▢</sub>"
    val unicode = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
    word.getSpanStart(unicode.toString())
    Log.d("getSpanStart", "${word.getSpanStart(unicode.toString())}")
    val builder = SpannableStringBuilder(word)
    for (char in word.indices)
    if (isSubUnicode(unicode)) {
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                Toast.makeText(widget.context, "ewwefwef", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setSpan(
            clickableSpan,
           45,
            46,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        builder.setSpan(
//            SubscriptSpan(),
//            45,
//           46,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
    }
    return builder
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
//fun main() {
////    for(wordIndex in spellingPref.indices) {
////        var words = spellingPref[wordIndex].replace("!", "")
////        words = transformWord(words).lowercase()
////        val markerWord = words
////
////        println("\"$markerWord\" to \"\",")
//    val spellingSeparateList = separateList
//    var randomWord = spellingSeparateList.random()
//    print(randomWord)
//
//}


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

fun getPairSpellingTwelve(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingTwelveList.forEach {
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
    context: Context,
    function: (Int, Char) -> Unit,
): SpannableStringBuilder {
    val builder = SpannableStringBuilder(word.lowercase(Locale.getDefault()))

    for (characterIndex in word.indices) {
        val character = word[characterIndex]
        if (isVowel(character)) {
            val clickableSpan = object : CustomClickableSpan(ContextCompat.getColor(context, android.R.color.holo_orange_light)) {
                override fun onClick(widget: View) {
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

fun replaceUnicodeWithComma(word: String): SpannableStringBuilder {
    val result = SpannableStringBuilder()

    for (char in word) {
        // Проверяем, является ли символ Unicode запятой
        if (Character.getType(char) == (Character.UNASSIGNED).toInt()) {
            // Если символ не запятая, заменяем его на запятую
            result.append(',')
        } else {
            // Если символ уже запятая, оставляем его без изменений
            result.append(char)
        }
    }

    return result
}

//для погдсветки букв в верхнем регистре на экране окончания игры
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
            val color = if (correctIndex) Color.parseColor("#82F25C") else Color.parseColor("#FF0404")

            if (i == selectedVowelIndex && selectedVowelChar != null) {
                if (currentChar.equals(selectedVowelChar, ignoreCase = true)) {
                    builder.setSpan(
                        ForegroundColorSpan(Color.parseColor("#82F25C")),
                        i,
                        i + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                } else {
                    builder.setSpan(
                        ForegroundColorSpan(Color.parseColor("#FF0404")),
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



