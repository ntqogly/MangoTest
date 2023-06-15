package com.example.mangotest.model.profiledata

import com.google.gson.annotations.SerializedName

data class Profiles(
    @SerializedName("profile_data") var profileData: ProfileData? = ProfileData()
)
