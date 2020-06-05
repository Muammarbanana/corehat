package com.example.corehat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {

    val data = MutableLiveData<String>()

    fun datasend(item: String) {
        data.value = item
    }
}