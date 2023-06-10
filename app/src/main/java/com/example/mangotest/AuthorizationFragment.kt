package com.example.mangotest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mangotest.databinding.FragmentAuthorizationBinding
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationRequest
import com.example.mangotest.model.sendauthcode.AuthNumberRequest
import com.example.mangotest.network.ApiFactory
import com.example.mangotest.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding
    private val apiService: ApiService = ApiFactory.getApiService()
    private val regFragment = RegistrationFragment()

//    private val viewModel: MyViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthorizationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPhoneNumberFormat()
        sendPhoneNumberAndHandleResponse()
    }

    private fun setupPhoneNumberFormat() {
        with(binding.countryCodePicker) {
            registerCarrierNumberEditText(binding.editTextPhoneNumber)
            setNumberAutoFormattingEnabled(true)
            val currentRegion = java.util.Locale.getDefault().country
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
                activity?.runOnUiThread {
                    if (isSuccess) {
                        showConfirmationCodeLayout()
                        checkCodeAndHandleResponse()
                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext,
                            "Ошибочка sendPhoneNumberAndHandleResponse",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showConfirmationCodeLayout() {
        binding.linearlayoutPhoneNumber.visibility = View.GONE
        binding.linearlayoutConfirmationCode.visibility = View.VISIBLE
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
                activity?.runOnUiThread {
                    val refreshToken = response.refresh_token
                    val accessToken = response.access_token
                    val userId = response.user_id
                    val isUserExists = response.is_user_exists
                    if (isUserExists) {
                        //Авторизация пользователя
                        TODO()
                    } else {
                        proceedToRegistration()
                    }
                }
            }
        }
    }

    private fun proceedToRegistration() {
        val countryCode = binding.countryCodePicker.selectedCountryCodeWithPlus
        Log.d("CountryCode", "Selected country code: $countryCode")
        val phoneNumber = binding.editTextPhoneNumber.text.toString()
        val bundle = Bundle()
        bundle.putString("phone_number", phoneNumber)
        bundle.putString("country_code", countryCode)
        regFragment.arguments = bundle
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.container, regFragment
        ).addToBackStack(null).commit()
    }
}