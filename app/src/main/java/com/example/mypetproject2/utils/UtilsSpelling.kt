package com.example.mypetproject2.utils

import android.app.AlertDialog
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.data.baselist.Rules
import com.example.mypetproject2.features.ui.games.stress.GamesViewModel
import com.example.mypetproject2.features.ui.games.stress.StressFragment
import java.lang.Exception

fun main() {
    print(transformWord("совершеННо"))
    print(transformWord("совершеНо"))
}

fun transformWord(word: String): String {
    var upperCaseCount = 0
    var transformedWord = ""

    for (char in word) {
        if (char.isUpperCase()) {
            upperCaseCount++
            if (upperCaseCount == 2) {
                continue
            }
        }
        transformedWord += char
    }

    return transformedWord
}

fun applyRule(word: String): String {
    val transformedWord = transformWord(word).lowercase()
    val suffixStart = transformedWord.indexOf('!') + 1
    val suffixEnd = transformedWord.lastIndexOf('!')
    if (suffixStart in 1 until suffixEnd) {
        val suffix = transformedWord.substring(suffixStart, suffixEnd)
        val rules = Rules.rules
        val rule = rules[suffix]
        return rule ?: ""
    }
    return ""
}

fun transformWordSuf(word: String): String {
    var upperCaseCount = 0
    var transformedWord = ""

    for (char in word) {
        if (char.isUpperCase()) {
            upperCaseCount++
            if (upperCaseCount == 2) {
                continue
            }
        }
        transformedWord += char
    }

    return transformedWord.replace(Regex("[#!@]")) { "" }
}

fun Fragment.setupOnBackPressedCallback() {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitConfirmationDialog()
            }
        })
}


private fun Fragment.showExitConfirmationDialog() {
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle("Выход из игры")
    builder.setMessage("Вы точно хотите выйти из игры?")
    builder.setPositiveButton("Да") { dialog, _ ->
        dialog.dismiss()
        findNavController().popBackStack()
    }
    builder.setNegativeButton("Нет") { dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}

fun getUserAnswers(viewModel: GamesViewModel): BooleanArray {
    val userAnswersList = viewModel.userAnswers.value ?: mutableListOf()
    return userAnswersList.toBooleanArray()
}

fun stringBuilder(word: String): SpannableStringBuilder {
    val startIndex = word.indexOf("!")
    val endIndex = word.indexOf("!", startIndex + 1)

    val spannableStringBuilder = SpannableStringBuilder(word.replace("!", ""))
        spannableStringBuilder.setSpan(
            StyleSpan(Typeface.BOLD),
            startIndex,
            endIndex - 1,
            0
        )
    return spannableStringBuilder

}

fun calculatePercentage(viewModel: GamesViewModel): Float {
    return (viewModel.score.value?.toFloat() ?: 0f) / StressFragment.MAX_ATTEMPTS * 100
}
