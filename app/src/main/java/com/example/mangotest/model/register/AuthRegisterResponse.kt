package com.example.mangotest.model.register

data class AuthRegisterResponse(
    val refresh_token: String,
    val access_token: String,
    val user_id: Int
)
