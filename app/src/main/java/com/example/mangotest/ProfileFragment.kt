package com.example.mangotest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mangotest.databinding.FragmentProfileBinding
import com.example.mangotest.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var bindinq: FragmentProfileBinding
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bindinq = FragmentProfileBinding.inflate(inflater, container, false)
        return bindinq.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfileData()

    }

    private fun loadProfileData() {
        viewModel.token.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                val response = ApiFactory.getApiService().getUserInfo("Bearer $it")
                requireActivity().runOnUiThread {
                    with(bindinq) {
                        with(response) {
                            tvPhoneNumber.text = profileData?.phone
                            tvNickName.text = profileData?.username
                            tvBirthDay.text = profileData?.birthday
                            tvStatus.text = profileData?.status
                            tvCity.text = profileData?.city
                        }
                    }
                }
            }
        }
    }
}