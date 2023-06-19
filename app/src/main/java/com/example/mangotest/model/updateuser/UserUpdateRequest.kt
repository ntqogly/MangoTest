package com.example.mangotest.model.updateuser

data class UserUpdateRequest(
    val birthday: String,
    val city: String,
    val name: String?,
    val status: String,
    val username: String?,
)