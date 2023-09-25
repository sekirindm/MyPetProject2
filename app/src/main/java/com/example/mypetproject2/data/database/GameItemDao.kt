package com.example.mypetproject2.data.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface GameItemDao {

    @Query("SELECT * FROM game_items")
    suspend fun getAllGameItems(): List<GameItemDb>

    @Query("SELECT * FROM game_items")
    fun getAllGameItems2(): LiveData<List<GameItemDb>>

    @Delete
    fun delete(item: GameItemDb)

    @Query("SELECT COUNT(*) FROM game_items WHERE rightAnswer = :word")
    fun isWordInDatabase(word: String): LiveData<Boolean>

    @Update
    suspend fun updateGameItems(gameItems: List<GameItemDb>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(gameItemDb: GameItemDb)
}

