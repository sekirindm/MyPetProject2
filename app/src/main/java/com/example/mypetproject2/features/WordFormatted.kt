package com.example.mypetproject2.features

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ClickableSpan
import android.view.View
import com.example.mypetproject2.features.ui.games.GamesFragment
import java.util.*

class WordFormatted {


     fun createSpannableStringBuilder(word: String): SpannableStringBuilder {
        val builder = SpannableStringBuilder(word.lowercase(Locale.getDefault()))

        for (characterIndex in word.indices) {
            val character = word[characterIndex]
            if (isVowel(character)) {
                val clickableSpan = createClickableSpan(characterIndex, character)
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

    // createClickableSpan(characterIndex, character): Создает ClickableSpan для заданного индекса и символа гласной буквы.
    // При клике на эту букву будет вызываться метод handleVowelClick(characterIndex, character).
     fun createClickableSpan(characterIndex: Int, character: Char): ClickableSpan {
        return object : ClickableSpan() {
            override fun onClick(view: View) {
               GamesFragment().handleVowelClick(characterIndex, character)
            }
        }
    }
}