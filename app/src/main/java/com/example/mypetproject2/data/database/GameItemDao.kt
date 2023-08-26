package com.example.mypetproject2.data.database

import androidx.room.*

@Dao
interface GameItemDao {

    @Query("SELECT * FROM game_items")
    suspend fun getAllGameItems(): List<GameItemDb>

    @Delete
    fun delete(item: GameItemDb)

    @Update
    suspend fun updateGameItems(gameItems: List<GameItemDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameItemDb: GameItemDb)
}