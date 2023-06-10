package com.example.mangotest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mangotest.databinding.ActivityMainBinding
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationRequest
import com.example.mangotest.model.sendauthcode.AuthNumberRequest
import com.example.mangotest.network.ApiFactory
import com.example.mangotest.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val apiService: ApiService = ApiFactory.getApiService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupPhoneNumberFormat()
        sendPhoneNumberAndHandleResponse()
//        setupConfirmationCode()

    }

    private fun setupPhoneNumberFormat() {
        with(binding.countryCodePicker) {
            registerCarrierNumberEditText(binding.editTextPhoneNumber)
            setNumberAutoFormattingEnabled(true)
            val currentRegion = Locale.getDefault().country
            setDefaultCountryUsingNameCode(currentRegion)
            resetToDefaultCountry()
        }
    }


    private fun sendPhoneNumberAndHandleResponse() {
        binding.button.setOnClickListener {
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.sendAuthNumber(AuthNumberRequest(phoneNumber))
                val isSuccess = response.is_success
                runOnUiThread {
                    if (isSuccess) {
                        showConfirmationCodeLayout()
                        checkCodeAndHandleResponse()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "Ошибочка sendPhoneNumberAndHandleResponse",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun checkCodeAndHandleResponse() {
        binding.btnConfirm.setOnClickListener {
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val confirmationCode = binding.editTextConfirmationCode.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.checkAuthCode(
                    AuthCodeVerificationRequest(
                        phoneNumber, confirmationCode
                    )
                )
                runOnUiThread {
                    val refreshToken = response.refresh_token
                    val accessToken = response.access_token
                    val userId = response.user_id
                    val isUserExists = response.is_user_exists
                    Toast.makeText(
                        applicationContext,
                        "refreshToken: $refreshToken, accessToken:$accessToken" + "userId:$userId, isUserExists:$isUserExists",
                        Toast.LENGTH_SHORT
                    ).show()
                }


//                if (response != null) {
//                    Log.d(
//                        "OnCreate",
//                        "refreshToken: $refreshToken, accessToken: $accessToken, userId: $userId, isUserExists: $isUserExists"
//                    )
//                    if (isUserExists) {
//                        // Authorize the user
//                        authorizeUser(userId, accessToken, refreshToken)
//                    } else {
//                        proceedToRegistration(userId, accessToken, refreshToken)
//                        TODO()
//                    }
//                } else {
//                    Toast.makeText(
//                        applicationContext, "Error verifying confirmation code", Toast.LENGTH_SHORT
//                    ).show()
//                }

            }
        }
    }


//    private fun authorizeUser(userId: Int, accessToken: String, refreshToken: String) {
//        // Perform the authorization logic
//        // Save the refresh token and access token for future use
//        // Replace TODO() with your code
//    }
//    private fun proceedToRegistration(userId: Int, accessToken: String, refreshToken: String) {
//        // Proceed with the registration process or navigate to the registration screen
//        // Replace TODO() with your code
//    }

    private fun showConfirmationCodeLayout() {
        binding.linearlayoutPhoneNumber.visibility = View.GONE
        binding.linearlayoutConfirmationCode.visibility = View.VISIBLE
    }
}

