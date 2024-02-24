package com.example.mypetproject2.utils

import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.content.ContextCompat
import com.example.mypetproject2.features.ui.ui.CustomClickableSpan
import java.util.Locale

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
            val clickableSpan = object : CustomClickableSpan(
                ContextCompat.getColor(
                    context,
                    android.R.color.holo_orange_light
                )
            ) {
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

fun isVowel(c: Char): Boolean {
    val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
    return c.lowercaseChar() in vowels
}