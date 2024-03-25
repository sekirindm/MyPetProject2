package com.example.mypetproject2.features.ui.home.minigame
//Используется для ListAdapter и DiffUtils. Нужен что бы хранить в себе дата классы каждого из айтема
// А так же удобен для использования в Adapter. Можно отталкиваться от state
sealed class HomeItemsList {

    data class HomeMiniGame(val miniGame: MiniGame) : HomeItemsList()

}
