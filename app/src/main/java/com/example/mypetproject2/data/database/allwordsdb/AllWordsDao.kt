package com.example.mypetproject2.data.database.allwordsdb

import android.util.Log
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AllWordsDao {

    @Query("SELECT * FROM all_words")
    suspend fun getAllWords(): List<AllWordsDb>

    @Query("DELETE FROM all_words")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(allWordsDb: AllWordsDb)

    @Query("SELECT count FROM all_words WHERE words = :word")
    suspend fun getWordCount(word: String): Int?

    @Query("SELECT * FROM all_words WHERE words = :word")
    suspend fun getWordsDb(word: String): AllWordsDb?

    @Query("SELECT EXISTS (SELECT 1 FROM all_words WHERE words = :word AND count < 5)")
    suspend fun doesWordMeetCriteria(word: String): Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM all_words WHERE words = :word)")
    suspend fun doesWordExist(word: String): Boolean?

    @Query("UPDATE all_words SET count = :newCount WHERE words = :word")
    fun updateWordCount(word: String, newCount: Int)

    @Update
    suspend fun updateWordCount(allWordsDb: AllWordsDb)


    @Transaction
    suspend fun insertSmart(word: String) {
        if (!doesWordExist(word)!!) {
            insert(AllWordsDb(word, 0))
        }
    }

    @Transaction
    suspend fun updateSmart(word: String, isRight: Boolean) {
        val wdb = getWordsDb(word) ?: return
        if (isRight) {
            updateWordCount(AllWordsDb(word, wdb.count + 1))
        } else {
            updateWordCount(AllWordsDb(word, 0))
        }
    }

//    @Query("SELECT EXISTS(SELECT 1 FROM all_words WHERE words = :word AND (count < 5 OR count IS NULL))")
//    fun isWordValid(word: String): Boolean


}