package com.example.mypetproject2.features.ui.games.stress.logic

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan

class GamesLogic {

    private var previousVowelIndex: Int = -1

    fun updateSelectedVowelFormatting(spannableStringBuilder: SpannableStringBuilder, selectedVowelIndex: Int, selectedVowelChar: Char?) {
        val spannableLength = spannableStringBuilder.length
        if (selectedVowelIndex in 0 until spannableLength && selectedVowelChar != null) {
            val character = selectedVowelChar.toString().uppercase()

            val previousVowelIndex = this.previousVowelIndex
            if (previousVowelIndex in 0 until spannableLength) {
                val previousVowelChar = spannableStringBuilder[previousVowelIndex]
                spannableStringBuilder.removeSpan(ForegroundColorSpan(Color.BLACK))
                spannableStringBuilder.replace(previousVowelIndex, previousVowelIndex + 1, previousVowelChar.lowercase().toString())
            }
            spannableStringBuilder.setSpan(
                ForegroundColorSpan(Color.BLACK),
                selectedVowelIndex,
                selectedVowelIndex + 1,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableStringBuilder.replace(selectedVowelIndex, selectedVowelIndex + 1, character)

            // Обновление предыдущего индекса выбранной гласной буквы
            this.previousVowelIndex = selectedVowelIndex
        }
    }


}