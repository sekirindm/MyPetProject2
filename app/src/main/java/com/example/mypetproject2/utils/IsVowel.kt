package com.example.mypetproject2.features

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import com.example.mypetproject2.data.stress
import java.util.*

fun isVowel(c: Char): Boolean {
    val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
    return c.lowercaseChar() in vowels
}

fun getPair(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    stress.forEach { stress ->
        userAnswers.forEach { answer ->
            if (stress.lowercase() == answer.lowercase()) map.add(Pair(stress, answer))
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

//    createForegroundColorSpan(i, correctIndex):
//    Создает ForegroundColorSpan для форматирования гласной буквы в зеленый или красный цвет в зависимости от ее правильности.
 fun createForegroundColorSpan(i: Int, correctIndex: Int): CharacterStyle {
    return if (i == correctIndex) {
        ForegroundColorSpan(Color.GREEN)
    } else {
        ForegroundColorSpan(Color.RED)
    }
}

