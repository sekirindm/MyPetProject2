package com.example.mypetproject2.utils

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mypetproject2.features.ui.games.choosecorrectword.choose.ChooseWordFragmentDirections
import com.example.mypetproject2.features.ui.games.choosecorrectword.gamenumber13.ChooseSeparateSpellingWordFragmentDirections
import com.example.mypetproject2.features.ui.games.choosecorrectword.gamenumber14.GameFourteenFragmentDirections
import com.example.mypetproject2.features.ui.games.paroynimandformation.ParonymAndFormationGameDirections
import com.example.mypetproject2.features.ui.games.punctuationgames.PunctuationGameOneFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingnn.SpellingNNFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingpref.SpellingPrefFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingroot.SpellingRootFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingsuffix.SpellingSuffixFragmentDirections
import com.example.mypetproject2.features.ui.games.spelling.spellingtwelve.GameTwelveFragmentDirections
import com.example.mypetproject2.features.ui.games.stress.StressFragmentDirections

fun Fragment.navigateToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = StressFragmentDirections.actionStressFragmentToGameFinishedFragment(
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

fun Fragment.navigateSpellingTwelveToGameFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action =  GameTwelveFragmentDirections.actionGameTwelveFragmentToGameFinishedFragment(
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

fun Fragment.navigateGameFourteenFragmentToFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = GameFourteenFragmentDirections.actionGameFourteenFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigatePunctuationFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = PunctuationGameOneFragmentDirections.actionPunctuationGameOneFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}

fun Fragment.navigateParonymGameFragmentToFinishedFragment(
    score: Int,
    percentage: Float,
    userAnswers: BooleanArray,
    userAnswersHistory: Array<String>,
    gameType: String
) {
    val action = ParonymAndFormationGameDirections.actionParonymGameFragmentToGameFinishedFragment(
        score,
        percentage,
        userAnswers,
        userAnswersHistory,
        gameType
    )
    findNavController().navigate(action)
}