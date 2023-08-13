package com.example.mypetproject2.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_items")
data class GameItemDb(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rightAnswer: String,
    val userAnswer: String,
    val rule: String

)
