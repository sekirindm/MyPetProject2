package com.example.mypetproject2.data.database.gamedb

import androidx.room.Entity
import androidx.room.PrimaryKey
import dagger.Module

@Entity(tableName = "game_items")
data class GameItemDb(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val rightAnswer: String,
    var position: Int
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is GameItemDb) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


