package com.example.mangotest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mangotest.databinding.FragmentRegistrationBinding
import com.example.mangotest.model.register.AuthRegisterRequest
import com.example.mangotest.model.register.AuthRegisterResponse
import com.example.mangotest.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Response

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    private val profileDetailFragment = ProfileDetailFragment()
    private val viewModel: MyViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences

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
        binding.buttonRegister.setOnClickListener {
            registerUser()
        }
        sharedPreferences = requireContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
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
        val numberWithoutCode = arguments?.getString("phone_number")
        val countryCode = arguments?.getString("country_code")
        val fullPhoneNumber =
            getString(R.string.formatted_phone_number, countryCode, numberWithoutCode)
        val name = binding.etName.text.toString()
        val userName = binding.etUserName.text.toString()
        val editor = sharedPreferences.edit()
        editor.putString(NAME, name)
        editor.putString(USER_NAME, userName)
        editor.apply()
        CoroutineScope(Dispatchers.IO).launch {
            val response = ApiFactory.getApiService().register(
                AuthRegisterRequest(fullPhoneNumber, name, userName)
            )
            val refreshToken = response.body()?.refresh_token
            val accessToken = response.body()?.access_token
            val userId = response.body()?.user_id
            activity?.runOnUiThread {
                viewModel.token.value = accessToken
                if (response.isSuccessful) {
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.container, profileDetailFragment)
                        addToBackStack(null)
                        commit()
                    }
                } else {
                    showError(response)
                }
            }
        }

    }

    private fun showError(response: Response<AuthRegisterResponse>) {
        val errorMessage = response.errorBody()?.string()?.let { it1 ->
            JSONObject(it1).getJSONObject("detail").getString("message")
        }
        Toast.makeText(
            requireActivity().applicationContext, "$errorMessage", Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        private const val NAME = "name"
        private const val USER_NAME = "user_name"
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }
}