package com.example.mypetproject2.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.features.ui.games.spelling.SpellingNNFragmentDirections
import com.example.mypetproject2.features.ui.games.stress.GamesFragmentDirections

fun Fragment.navigateToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = GamesFragmentDirections.actionNavigationDashboardToGameFinishedFragment(
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