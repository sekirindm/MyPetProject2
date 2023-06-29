package com.example.mypetproject2.features

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import com.example.mypetproject2.data.spellingNN
import com.example.mypetproject2.data.stress
import java.util.*


fun isVowel(c: Char): Boolean {
    val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
    return c.lowercaseChar() in vowels
}


/**
 * нам нужен метод gatPairSpelling которыйц будет сравнивать слово из списка с сответом пользователя.
 * 1. Ответ пользователя нужно передавать в параметры(List<String>).
 * 2. Возвращает List<Pair<String, String>>, где первое слово является словом из списка, а второе - ответом пользователя.
 * 3. для каждого ответа пользователя С помощью цикла forEach, ищем соответсвующее слово из списка
 * 3.1
 *
 * */

//fun main() {
//    val word = "dskfqqwertyuioplkjhgfdsazxcvbnmadrgawwwdfrrdegdfdcfxzrett"
//    print(word.toSortedSet().joinToString(""))
//
//}
fun getPairSpelling(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answers ->
        spellingNN.forEach {
            if (answers.replace("Н", "") == it.replace("Н", "")) map.add(Pair(it, answers ))
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



