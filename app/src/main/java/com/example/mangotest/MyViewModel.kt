package com.example.mangotest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

//    private val _userInputPhoneNumber = MutableLiveData<String>()
//    val userInputPhoneNumber: LiveData<String>
//        get() = _userInputPhoneNumber
//
//    fun setUserInput(inputPhoneNumber: String) {
//        _userInputPhoneNumber.value = inputPhoneNumber
//    }

    val token = MutableLiveData<String>()
}