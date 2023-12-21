package com.example.mypetproject2.features.ui.games.stress.logic

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import com.example.mypetproject2.features.isVowel
import java.util.*

class SpannableStringBuilderHelper {

    /**
     * createResultSpannableStringBuilder(word, correctIndex): Создает SpannableStringBuilder для отображения результатов теста.
     * Выделяет правильную и неправильную гласные буквы в зеленый и красный цвета соответственно. Также выделяет выбранную гласную букву.
     * */

    fun createResultSpannableStringBuilder(
        word: String,
        correctIndex: Int,
        selectedVowelIndex: Int?,
        selectedVowelChar: Char?,
    ): SpannableStringBuilder {
        val builder = SpannableStringBuilder(word.lowercase(Locale.ROOT))

        for (i in word.indices) {
            val character = word[i]
            if (isVowel(character)) {
                val span = createForegroundColorSpan(i, correctIndex)
                builder.setSpan(span, i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }

        selectedVowelIndex?.let {
            selectedVowelChar?.let { char ->
                val selectedCharIndex = word.indexOf(char)
                val color = if (selectedCharIndex == correctIndex) Color.parseColor(colorGreen) else Color.parseColor(
                    colorRed)

                if (selectedCharIndex != -1 && selectedCharIndex == selectedVowelIndex) {
                    builder.setSpan(
                        ForegroundColorSpan(color),
                        selectedCharIndex,
                        selectedCharIndex + 1,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    )
                    builder.replace(
                        selectedCharIndex,
                        selectedCharIndex + 1,
                        char.toString().uppercase()
                    )
                }
            }
        }

        return builder
    }

    private fun createForegroundColorSpan(index: Int, correctIndex: Int): ForegroundColorSpan {
        val color = if (index == correctIndex) Color.GREEN else Color.RED
        return ForegroundColorSpan(color)
    }

    private companion object {
        const val colorRed = "#FF0404"
        const val colorGreen = "#82F25C"
    }
}




