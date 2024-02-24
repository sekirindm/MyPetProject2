package com.example.mypetproject2.data.database

import android.content.Context
import androidx.room.*
import com.example.mypetproject2.data.database.allwordsdb.AllWordsDao
import com.example.mypetproject2.data.database.allwordsdb.AllWordsDb
import com.example.mypetproject2.data.database.gamedb.GameItemDao
import com.example.mypetproject2.data.database.gamedb.GameItemDb

@Database(entities = [GameItemDb::class, AllWordsDb::class],  version = 3)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var instance: AppDatabase? = null
        private const val DATABASE_NAME = "main.db"

        fun getInstance(context: Context): AppDatabase {
            synchronized(this) {
                return instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build().also {
                    instance = it
                }
            }
        }
    }

    abstract fun gameItemDao(): GameItemDao

    abstract fun allWordsDao(): AllWordsDao
}
