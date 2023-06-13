package com.example.mypetproject2.features.ui.games

import com.example.mypetproject2.data.stress

fun generateNewWords(currentIndex: Int): List<String> {
    val end = minOf(currentIndex + 3, stress.size)
    return stress.subList(currentIndex, end)
}
