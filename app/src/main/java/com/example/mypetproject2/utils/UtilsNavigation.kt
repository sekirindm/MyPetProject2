package com.example.mypetproject2.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.features.ui.games.choose.ChooseWordFragmentDirections
import com.example.mypetproject2.features.ui.games.choosespelling.ChooseSeparateSpellingWordFragment
import com.example.mypetproject2.features.ui.games.choosespelling.ChooseSeparateSpellingWordFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingnn.SpellingNNFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingpref.SpellingPrefFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingroot.SpellingRootFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingsuffix.SpellingSuffixFragmentDirections
import com.example.mypetproject2.features.ui.games.stress.GamesFragmentDirections

fun Fragment.navigateToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = GamesFragmentDirections.actionGamesFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigateSpellingToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = SpellingNNFragmentDirections.actionSpellingNNFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigateSpellingPrefToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = SpellingPrefFragmentDirections.actionSpellingPrefFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigateSpellingRootToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = SpellingRootFragmentDirections.actionSpellingRootFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}
fun Fragment.navigateSpellingSuffixToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = SpellingSuffixFragmentDirections.actionSpellingSuffixFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigateChooseWordFragmentToFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = ChooseWordFragmentDirections.actionChooseWordFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigateChooseSeparateWordFragmentToFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = ChooseSeparateSpellingWordFragmentDirections.actionChooseWordFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}