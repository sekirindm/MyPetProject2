package com.example.mypetproject2.data.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [GameItemDb::class], version = 1)
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
}
