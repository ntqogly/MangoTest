package com.example.mangotest.network

import com.example.mangotest.model.sendauthcode.AuthNumberRequest
import com.example.mangotest.model.sendauthcode.AuthNumberResponse
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationRequest
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationResponse
import com.example.mangotest.model.register.AuthRegisterRequest
import com.example.mangotest.model.register.AuthRegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/users/send-auth-code/")
    suspend fun sendAuthNumber(@Body request: AuthNumberRequest): AuthNumberResponse

    @POST("api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: AuthCodeVerificationRequest): AuthCodeVerificationResponse

    @POST("api/v1/users/register/")
    suspend fun register(@Body request: AuthRegisterRequest): AuthRegisterResponse
}