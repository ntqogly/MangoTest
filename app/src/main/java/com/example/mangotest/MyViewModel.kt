package com.example.mangotest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    private val _userInputPhoneNumber = MutableLiveData<String>()
    val userInputPhoneNumber: LiveData<String>
        get() = _userInputPhoneNumber

    fun setUserInput(inputPhoneNumber: String) {
        _userInputPhoneNumber.value = inputPhoneNumber
    }
}