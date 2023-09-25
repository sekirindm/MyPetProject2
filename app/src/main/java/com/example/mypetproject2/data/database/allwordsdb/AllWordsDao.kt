package com.example.mypetproject2.data.database.allwordsdb

import androidx.room.*

@Dao
interface AllWordsDao {

    @Query("SELECT * FROM all_words")
    suspend fun getAllWords(): List<AllWordsDb>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(allWordsDb: AllWordsDb)

    @Query("SELECT count FROM all_words WHERE words = :word")
    suspend fun getWordCount(word: String): Int?


    @Query("UPDATE all_words SET count = :newCount WHERE words = :word")
    fun updateWordCount(word: String, newCount: Int)

    @Update
    suspend fun updateWordCount(allWordsDb: AllWordsDb)

//    @Query("SELECT EXISTS(SELECT 1 FROM all_words WHERE words = :word AND (count < 5 OR count IS NULL))")
//    fun isWordValid(word: String): Boolean


}