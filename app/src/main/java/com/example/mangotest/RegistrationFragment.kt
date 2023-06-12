package com.example.mangotest

import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mangotest.databinding.FragmentRegistrationBinding
import com.example.mangotest.model.register.AuthRegisterRequest
import com.example.mangotest.model.register.AuthRegisterResponse
import com.example.mangotest.network.ApiFactory
import com.example.mangotest.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val apiService: ApiService = ApiFactory.getApiService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInputFilters(binding.etUserName)
        getInputPhoneNumber()
        registerUser()
    }

    private fun setInputFilters(editText: EditText) {
        val filter = InputFilter { source, _, _, _, _, _ ->
            for (i in source.indices) {
                if (!isValidCharacter(source[i])) {
                    return@InputFilter ""
                }
            }
            null
        }
        editText.filters = arrayOf(filter)
    }

    private fun isValidCharacter(ch: Char): Boolean {
        return ch in 'A'..'Z' || ch in 'a'..'z' || ch in '0'..'9' || ch == '-' || ch == '_'
    }

    private fun getInputPhoneNumber() {
        val numberWithoutCode = arguments?.getString("phone_number")
        val countryCode = arguments?.getString("country_code")
        if (numberWithoutCode != null && countryCode != null) {
            val formattedFullNumber =
                getString(R.string.formatted_code_plus_number, countryCode, numberWithoutCode)
            binding.tvPhoneNumber.text = formattedFullNumber
        }
    }

    private fun registerUser() {
        binding.buttonRegister.setOnClickListener {
            val numberWithoutCode = arguments?.getString("phone_number")
            val countryCode = arguments?.getString("country_code")
            val fullPhoneNumber =
                getString(R.string.formatted_phone_number, countryCode, numberWithoutCode)
            val name = binding.etName.text.toString()
            val userName = binding.etUserName.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                val response = apiService.register(
                    AuthRegisterRequest(fullPhoneNumber, name, userName)
                )
                val refreshToken = response.refresh_token
                val accessToken = response.access_token
                val userId = response.user_id
                activity?.runOnUiThread {
                    if (refreshToken.isNotEmpty() and accessToken.isNotEmpty()) {
                        TODO() //авторизация
                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext, "Ошибка", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}