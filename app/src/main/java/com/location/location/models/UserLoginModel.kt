package com.location.location.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserLoginModel(
    @SerializedName("token")
    var token: String? = null, // 1212121212
): Serializable