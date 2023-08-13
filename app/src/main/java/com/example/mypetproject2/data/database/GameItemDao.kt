package com.example.mypetproject2.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameItemDao {

    @Query("SELECT * FROM game_items")
    suspend fun getAllGameItems(): List<GameItemDb>

    @Insert()
    suspend fun insert(gameItemDb: GameItemDb)
}