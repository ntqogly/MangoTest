package com.example.mangotest

import com.example.mangotest.model.AuthCodeRequest
import com.example.mangotest.model.AuthCodeVerificationRequest
import com.example.mangotest.model.AuthCodeVerificationResponse
import com.example.mangotest.network.ApiService

class AuthRepository(private val apiService: ApiService) {

    suspend fun sendAuthCode(phoneNumber: String): Boolean {
        return try {
            val request =AuthCodeRequest(phoneNumber)
            val response = apiService.sendAuthCode(request)
            response.is_success
        } catch (e: Exception) {
            // Handle the exception and show an error message
            e.printStackTrace()
            false
        }
    }

    suspend fun checkAuthCode(phoneNumber: String, code: String): AuthCodeVerificationResponse? {
        return try {
            val request = AuthCodeVerificationRequest(phoneNumber, code)
            val response = apiService.checkAuthCode(request)
            response
        } catch (e: Exception) {
            // Handle the exception and show an error message
            e.printStackTrace()
            null
        }
    }
}