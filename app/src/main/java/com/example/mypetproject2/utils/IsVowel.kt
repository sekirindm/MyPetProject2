package com.example.mypetproject2.features

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.*
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.mypetproject2.R
import com.example.mypetproject2.data.*
import com.example.mypetproject2.data.baselist.fourteenList
import com.example.mypetproject2.data.baselist.listPunctuationGameFive
import com.example.mypetproject2.data.baselist.listPunctuationGameFour
import com.example.mypetproject2.data.baselist.listPunctuationGameThree
import com.example.mypetproject2.data.baselist.listPunctuationGameTwo
import com.example.mypetproject2.data.baselist.listTriple
import com.example.mypetproject2.data.baselist.paronymList
import com.example.mypetproject2.data.baselist.separateList
import com.example.mypetproject2.data.baselist.spellingNN
import com.example.mypetproject2.data.baselist.spellingPref
import com.example.mypetproject2.data.baselist.spellingRoot
import com.example.mypetproject2.data.baselist.spellingSuffix
import com.example.mypetproject2.data.baselist.spellingTwelveList
import com.example.mypetproject2.data.baselist.stress
import com.example.mypetproject2.utils.transformWordSuf
import com.example.mypetproject2.features.ui.ui.CustomClickableSpan
import com.example.mypetproject2.features.ui.ui.NoUnderlineClickableSpan
import java.util.*

/**
 * нам нужен метод gatPairSpelling который будет сравнивать слово из списка с сответом пользователя.
 * 1. Ответ пользователя нужно передавать в параметры(List<String>).
 * 2. Возвращает List<Pair<String, String>>, где первое слово является словом из списка, а второе - ответом пользователя.
 * 3. для каждого ответа пользователя С помощью цикла forEach, ищем соответсвующее слово из списка
 * 3.1
 *
 * */
fun isSubUnicode(s: Char): Boolean {
    val htmlText = "▢"
    return s in htmlText

}

fun getPairSpelling(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answers ->
        spellingNN.forEach {
            if (answers.replace("Н", "") == it.replace("Н", "")) map.add(Pair(it, answers))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingPref(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingPref.forEach {
            if (answer.replace(Regex("[А-Я]")) { "" } == it.replace(Regex("[А-Я]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairPunctuation(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        listPunctuationGameTwo.forEach {
            if (answer.replace(Regex("[,▢]")) { "" } == it.replace(Regex("[|@#,]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
        listPunctuationGameThree.forEach {
            if (answer.replace(Regex("[,▢]")) { "" } == it.replace(Regex("[|@#,]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
        listPunctuationGameFour.forEach {
            if (answer.replace(Regex("[,▢]")) { "" } == it.replace(Regex("[|@#,]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
        listPunctuationGameFive.forEach {
            if (answer.replace(Regex("[,▢]")) { "" } == it.replace(Regex("[#,]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
        Log.d("userAnswers", answer.replace(Regex("[,▢]")) { "" })
        Log.d("listPunctuationGameTwo", answer.replace(Regex("[,▢]")) { "" })
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingRoot(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingRoot.forEach {
            if (answer.replace(Regex("[А-Я@]")) { "" } == it.replace(Regex("[А-Я@]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingSuffix(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingSuffix.forEach {
            if (answer.replace(Regex("[А-Я!]")) { "" } == it.replace(Regex("[А-Я!]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairSpellingTwelve(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        spellingTwelveList.forEach {
            if (answer.replace(Regex("[А-Я!]")) { "" } == it.replace(Regex("[А-Я!]")) { "" })
                map.add(Pair(transformWordSuf(it), answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairChooseWord(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        listTriple.forEach {
            if (answer == it.first || answer == it.second || answer == it.third) {
                map.add(Pair(transformWordSuf(it.first), answer))
            }
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairChooseSeparateWord(userAnswers: List<String>): List<Pair<String, String>> {

    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        separateList.forEach {

            val formattedFirst = when (it.second) {
                0 -> it.first.replace("(", "").replace(")", " ")
                1 -> it.first.replace("(", "").replace(")", "")
                else -> it.first.replace("(", "").replace(")", "-")
            }

            Log.d("answer", answer)
            Log.d("formattedFirst", formattedFirst)
            if (answer.replace(" ", "").replace("-", "") == formattedFirst.replace(" ", "")
                    .replace("-", "").replace("!", "")
            ) {

                map.add((Pair(formattedFirst.replace("!", ""), answer)))
            }
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairGameFourteen(userAnswers: List<String>): List<Pair<String, String>> {

    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        fourteenList.forEach {
            val formattedFirst = when (it.second) {
                0 -> it.first.replace("(", "").replace(")", " ")
                1 -> it.first.replace("(", "").replace(")", "")
                else -> it.first.replace("(", "").replace(")", "-")
            }
            Log.d("answer", answer)
            Log.d("formattedFirst", formattedFirst)
            if (answer.replace(" ", "").replace("-", "")
                == formattedFirst
                    .replace(" ", "")
                    .replace("-", "")
                    .replace("!", "")
            ) { map.add((Pair(formattedFirst.replace("!", ""), answer))) }
        }
    }
    Log.d("getPair", "map $map")
    return map
}

fun getPairParonym(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        paronymList.forEach {
            Log.d("answer", answer.toString())
            Log.d("paronymList", it.first)
            if (answer.replace(Regex("[А-Я!]")) { "" } == it.first.replace(Regex("[А-Я!]")) { "" }) {

                map.add(Pair(it.first, answer))
            }

        }
    }
    Log.d("getPair", "map $map")
    return map
}


fun getPair(userAnswers: List<String>): List<Pair<String, String>> {

    val map = mutableListOf<Pair<String, String>>()
    userAnswers.forEach { answer ->
        stress.forEach {
            if (answer.lowercase() == it.lowercase()) map.add(Pair(it, answer))
        }
    }
    Log.d("getPair", "map $map")
    return map
}


