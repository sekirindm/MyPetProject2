package com.example.mypetproject2.features.ui.home.minigame
//хранит в себе объекты, которые мы отображаем на айтеме
data class MiniGame(
    val word: String,
    val letters: List<String>,
    val rightAnswer: Boolean?
)