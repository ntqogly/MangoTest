package com.example.mangotest.model.checkauthcode

data class AuthCodeVerificationRequest(
    val phone: String, val code: String
)
