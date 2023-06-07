package com.example.mangotest

import android.app.Application
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.example.mangotest.databinding.ActivityMainBinding
import com.google.i18n.phonenumbers.PhoneNumberUtil
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countryCodePicker.registerCarrierNumberEditText(binding.editTextPhoneNumber)
        binding.countryCodePicker.setNumberAutoFormattingEnabled(true)

        binding.countryCodePicker.setOnCountryChangeListener {
            Timber.d("Selected country: ${binding.countryCodePicker.selectedCountryName}")
        }
        binding.editTextPhoneNumber.addTextChangedListener(phoneNumberWatcher)

    }

    private val phoneNumberWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            // Not needed for this example
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            // Not needed for this example
        }

        override fun afterTextChanged(s: Editable?) {
            val phoneNumber = s.toString()
            val countryCode = binding.countryCodePicker.selectedCountryCode

            val phoneNumberType = determinePhoneNumberType(countryCode, phoneNumber)
            Timber.d("Phone Number Type: $phoneNumberType")
        }
    }

    fun determinePhoneNumberType(countryCode: String, phoneNumber: String): String {
        val phoneNumberUtil = PhoneNumberUtil.getInstance()
        val fullPhoneNumber = "$countryCode$phoneNumber"

        try {
            val parsedPhoneNumber = phoneNumberUtil.parse(fullPhoneNumber, null)
            val phoneNumberType = phoneNumberUtil.getNumberType(parsedPhoneNumber)

            return when (phoneNumberType) {
                PhoneNumberUtil.PhoneNumberType.FIXED_LINE -> "Fixed Line"
                PhoneNumberUtil.PhoneNumberType.MOBILE -> "Mobile"
                PhoneNumberUtil.PhoneNumberType.FIXED_LINE_OR_MOBILE -> "Fixed Line or Mobile"
                PhoneNumberUtil.PhoneNumberType.TOLL_FREE -> "Toll Free"
                PhoneNumberUtil.PhoneNumberType.PREMIUM_RATE -> "Premium Rate"
                PhoneNumberUtil.PhoneNumberType.SHARED_COST -> "Shared Cost"
                PhoneNumberUtil.PhoneNumberType.VOIP -> "VoIP"
                PhoneNumberUtil.PhoneNumberType.PERSONAL_NUMBER -> "Personal Number"
                PhoneNumberUtil.PhoneNumberType.PAGER -> "Pager"
                PhoneNumberUtil.PhoneNumberType.UAN -> "UAN"
                PhoneNumberUtil.PhoneNumberType.VOICEMAIL -> "Voicemail"
                PhoneNumberUtil.PhoneNumberType.UNKNOWN -> "Unknown"
                else -> "Invalid Number"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return "Invalid Number"
        }
    }
}