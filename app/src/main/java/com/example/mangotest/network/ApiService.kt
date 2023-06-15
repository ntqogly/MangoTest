package com.example.mangotest.network

import com.example.mangotest.model.checkauthcode.AuthCodeVerificationRequest
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationResponse
import com.example.mangotest.model.profiledata.ProfileData
import com.example.mangotest.model.profiledata.Profiles
import com.example.mangotest.model.register.AuthRegisterRequest
import com.example.mangotest.model.register.AuthRegisterResponse
import com.example.mangotest.model.sendauthcode.AuthNumberRequest
import com.example.mangotest.model.sendauthcode.AuthNumberResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {

    @POST("api/v1/users/send-auth-code/")
    suspend fun sendAuthNumber(@Body request: AuthNumberRequest): AuthNumberResponse

    @POST("api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(@Body request: AuthCodeVerificationRequest): Response<AuthCodeVerificationResponse>

    @POST("api/v1/users/register/")
    suspend fun register(@Body request: AuthRegisterRequest): Response<AuthRegisterResponse>

    @GET("/api/v1/users/me/")
    suspend fun getUserInfo(@Header("Authorization") authorization: String): Profiles
}

