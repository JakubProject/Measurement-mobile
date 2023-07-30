package com.university.healthapp.ui.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FoodModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Food list"
    }
    val text: LiveData<String> = _text
}