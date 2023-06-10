package com.example.mangotest.network

import com.example.mangotest.model.sendauthcode.AuthNumberRequest
import com.example.mangotest.model.sendauthcode.AuthNumberResponse
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationRequest
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/users/send-auth-code/")
    suspend fun sendAuthNumber(@Body request: AuthNumberRequest): AuthNumberResponse

    @POST("api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: AuthCodeVerificationRequest): AuthCodeVerificationResponse
}