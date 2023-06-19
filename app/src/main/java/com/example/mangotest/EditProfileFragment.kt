package com.example.mangotest

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mangotest.databinding.FragmentProfileEditBinding
import com.example.mangotest.model.updateuser.UserUpdateRequest
import com.example.mangotest.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileFragment : Fragment() {

    private lateinit var bindinq: FragmentProfileEditBinding
    private val viewModel: MyViewModel by activityViewModels()
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bindinq = FragmentProfileEditBinding.inflate(inflater, container, false)
        return bindinq.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getNumberAndNickName()
        bindinq.buttonSaveProfileData.setOnClickListener {
            saveEditProfileData()
        }
        sharedPreferences = requireContext().getSharedPreferences("Prefs", Context.MODE_PRIVATE)
    }

    private fun getNumberAndNickName() {
        viewModel.token.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiFactory.getApiService().getUserInfo("Bearer $it")
                requireActivity().runOnUiThread {
                    with(bindinq) {
                        with(response) {
                            tvPhoneNumber.text = profileData?.phone
                            tvNickName.text = profileData?.username
                        }
                    }
                }
            }
        }
    }

    private fun saveEditProfileData() {
        viewModel.token.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiFactory.getApiService().updateUserProfile(
                    "Bearer $it", UserUpdateRequest(
                        bindinq.edBirthDay.text.toString(),
                        bindinq.etCity.text.toString(),
                        sharedPreferences.getString(NAME, null),
                        bindinq.etStatus.text.toString(),
                        sharedPreferences.getString(USER_NAME, null)
                    )
                )
                activity?.runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            requireActivity().applicationContext, "Success", Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireActivity().applicationContext, "errorMessage", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    companion object {
        private const val NAME = "name"
        private const val USER_NAME = "user_name"
    }
}