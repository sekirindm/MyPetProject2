package com.example.mypetproject2.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.features.ui.games.GamesFragmentDirections

 fun Fragment.navigateToGameFinishedFragment(
     score: Int,
     percentage: Float,
     userAnswers: BooleanArray,
     userAnswersHistory: Array<String>
) {
    val action = GamesFragmentDirections.actionNavigationDashboardToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory
    )
    findNavController().navigate(action)
}