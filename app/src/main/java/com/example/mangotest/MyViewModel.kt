package com.example.mangotest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    val token = MutableLiveData<String>()
    
}