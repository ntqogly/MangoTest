package com.example.mangotest.model.profiledata

import com.google.gson.annotations.SerializedName

data class ProfileData(
    @SerializedName("name") var name: String? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("birthday") var birthday: String? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("vk") var vk: String? = null,
    @SerializedName("instagram") var instagram: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("avatar") var avatar: String? = null,
    @SerializedName("last") var last: String? = null,
    @SerializedName("online") var online: Boolean? = null,
    @SerializedName("created") var created: String? = null,
    @SerializedName("phone") var phone: String? = null,
    @SerializedName("completed_task") var completedTask: Int? = null,
    @SerializedName("avatars") var avatars: Avatars? = Avatars(),
    @SerializedName("id") var id: Int? = null
)
