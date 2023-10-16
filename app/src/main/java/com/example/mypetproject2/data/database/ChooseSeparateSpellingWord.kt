package com.example.mypetproject2.data.database

import com.example.mypetproject2.data.database.SeparateSpellingNumber.SPACE

enum class SeparateSpellingNumber {
    SPACE,
    TOGETHER,
    DASH
}

 val separateList = listOf<Pair<String, SeparateSpellingNumber>>(
     "(не)верно" to SPACE,
 )