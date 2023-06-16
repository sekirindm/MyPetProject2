package com.example.mypetproject2.features

import android.app.Activity
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.CharacterStyle
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import com.example.mypetproject2.R
import com.example.mypetproject2.data.stress
import com.example.mypetproject2.features.ui.games.GamesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
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


fun createCustomResultSpannableStringBuilder(
    word: String,
    correctIndex: Int,
    selectedVowelIndex: Int?,
    selectedVowelChar: Char?
): SpannableStringBuilder {
    val builder = SpannableStringBuilder(word)
    for (i in word.indices) {
        val currentChar = word[i]

        if (currentChar.isUpperCase()) {
            val color = if (i == correctIndex) Color.GREEN else Color.RED

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

