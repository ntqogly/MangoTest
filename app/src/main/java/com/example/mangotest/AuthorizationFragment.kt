package com.example.mangotest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mangotest.databinding.FragmentAuthorizationBinding
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationRequest
import com.example.mangotest.model.checkauthcode.AuthCodeVerificationResponse
import com.example.mangotest.model.sendauthcode.AuthNumberRequest
import com.example.mangotest.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class AuthorizationFragment : Fragment() {

    private lateinit var binding: FragmentAuthorizationBinding
    private val regFragment = RegistrationFragment()

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
        binding.buttonSendNumber.setOnClickListener {
            binding.buttonSendNumber.isEnabled = false
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response =
                    ApiFactory.getApiService().sendAuthNumber(AuthNumberRequest(phoneNumber))
                val isSuccess = response.is_success
                activity?.runOnUiThread {
                    binding.buttonSendNumber.isEnabled = true
                    if (isSuccess) {
                        showConfirmationCodeLayout()
                        onBackPressed()
                        checkCodeAndHandleResponse()
                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext, "Ошибка", Toast.LENGTH_SHORT
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
            binding.btnConfirm.isEnabled = false
            val phoneNumber = binding.editTextPhoneNumber.text.toString()
            val confirmationCode = binding.editTextConfirmationCode.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiFactory.getApiService().checkAuthCode(
                    AuthCodeVerificationRequest(
                        phoneNumber, confirmationCode
                    )
                )
                binding.btnConfirm.isEnabled = false
                activity?.runOnUiThread {
                    val refreshToken = response.body()?.refresh_token
                    val accessToken = response.body()?.access_token
                    val userId = response.body()?.user_id
                    val isUserExists = response.body()?.is_user_exists
                    if (response.isSuccessful) {
                        if (isUserExists == true) {
                            //Авторизация пользователя
                            TODO()
                        } else {
                            proceedToRegistrationScreen()
                        }
                    } else {
                        showError(response)
                    }

                }
            }
        }
    }

    private fun showError(response: Response<AuthCodeVerificationResponse>) {
        val errorMessage = response.errorBody()?.string()?.let { it1 ->
            JSONObject(it1).getJSONObject("detail").getString("message")
        }
        Toast.makeText(
            requireActivity().applicationContext, "$errorMessage", Toast.LENGTH_SHORT
        ).show()
    }

    private fun proceedToRegistrationScreen() {
        val countryCode = binding.countryCodePicker.selectedCountryCodeWithPlus
        val phoneNumber = binding.editTextPhoneNumber.text.toString()
        regFragment.apply {
            arguments = Bundle().apply {
                putString("phone_number", phoneNumber)
                putString("country_code", countryCode)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, regFragment)
            addToBackStack(null)
            commit()
        }
    }

    private fun onBackPressed() {
        binding.btnBack.setOnClickListener {
            binding.linearlayoutPhoneNumber.visibility = View.VISIBLE
            binding.linearlayoutConfirmationCode.visibility = View.GONE
        }
    }

}