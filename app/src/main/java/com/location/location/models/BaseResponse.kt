package com.location.location.models

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("status")
    val isSuccess: Boolean = false,
    @SerializedName("message")
    var message: String = "",
    @SerializedName("is_login")
    var isLogin: Boolean = true,
    @SerializedName("result")
    var result: T? = null
)
