package com.example.mangotest.network

import com.example.mangotest.model.AuthCodeRequest
import com.example.mangotest.model.AuthCodeResponse
import com.example.mangotest.model.AuthCodeVerificationRequest
import com.example.mangotest.model.AuthCodeVerificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(request: AuthCodeRequest): AuthCodeResponse

    @POST("api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: AuthCodeVerificationRequest): AuthCodeVerificationResponse
}