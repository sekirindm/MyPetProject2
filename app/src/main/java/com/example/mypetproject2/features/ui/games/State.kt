package com.example.mypetproject2.features.ui.games
    data class State(
        val score: Int,
        val answers: MutableList<Pair<String, String>> = mutableListOf()
    )
