package com.example.mangotest

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.mangotest.databinding.FragmentRegistrationBinding
import java.lang.StringBuilder

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInputFilters(binding.editTextUserName)
        getInputPhoneNumber()
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
        val phoneNumber = arguments?.getString("phone_number")
        val countryCode = arguments?.getString("country_code")
        if (!phoneNumber.isNullOrEmpty() && !countryCode.isNullOrEmpty()) {
            val formattedPhoneNumber =
            getString(R.string.formatted_phone_number, countryCode, phoneNumber)
            binding.tvPhoneNumber.text = formattedPhoneNumber
        }
    }
}