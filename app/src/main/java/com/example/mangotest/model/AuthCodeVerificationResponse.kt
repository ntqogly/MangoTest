package com.example.mangotest.model

data class AuthCodeVerificationResponse(
    val refresh_token: String,
    val access_token: String,
    val user_id: Int,
    val is_user_exists: Boolean
)
