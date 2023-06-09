package com.example.mypetproject2.features

import com.example.mypetproject2.data.stress

fun isVowel(c: Char): Boolean {
    val vowels = listOf('а', 'е', 'ё', 'и', 'о', 'у', 'ы', 'э', 'ю', 'я')
    return c.lowercaseChar() in vowels
}

fun getPair(userAnswers: List<String>): List<Pair<String, String>> {
    val map = mutableListOf<Pair<String, String>>()
    stress.forEach { stress ->
        userAnswers.forEach { answer ->
            if (stress.lowercase() == answer.lowercase()) map.add(Pair(stress, answer))
        }
    }
    return map
}

fun check(list: List<Pair<String, String>>) = list.map { it.first == it.second }