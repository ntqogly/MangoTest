package com.example.mangotest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.mangotest.databinding.FragmentProfileDetailBinding
import com.example.mangotest.network.ApiFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileDetailFragment : Fragment() {

    private lateinit var bindinq: FragmentProfileDetailBinding
    private val editProfileFragment = EditProfileFragment()
    private val viewModel: MyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bindinq = FragmentProfileDetailBinding.inflate(inflater, container, false)
        return bindinq.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadProfileData()
        bindinq.buttonEditProfileItem.setOnClickListener {
            editProfileItem()
        }
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

    private fun editProfileItem() {
        val phoneNumber = bindinq.tvPhoneNumber.toString()
        val nickName = bindinq.tvNickName.toString()
        editProfileFragment.apply {
            arguments = Bundle().apply {
                putString("phone_number", phoneNumber)
                putString("nick_name", nickName)
            }
        }
        requireActivity().supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, editProfileFragment)
            addToBackStack(null)
            commit()
        }
    }
}