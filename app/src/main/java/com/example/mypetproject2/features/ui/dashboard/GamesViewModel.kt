package com.example.mypetproject2.features.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GamesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is games Fragment"
    }
    val text: LiveData<String> = _text
}