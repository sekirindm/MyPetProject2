package com.example.mypetproject2.data.database.allwordsdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_words")
data class AllWordsDb(
    @PrimaryKey
    val words: String,
    var count: Int
)
