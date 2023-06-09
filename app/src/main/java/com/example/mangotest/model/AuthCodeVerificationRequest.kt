package com.example.mangotest.model

data class AuthCodeVerificationRequest(
    val phone: String, val code: String
)
