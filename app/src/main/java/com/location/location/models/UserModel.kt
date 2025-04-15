package com.location.location.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class UserModel(

    @SerializedName("address")
    var address: String? = "",
    @SerializedName("country")
    var country: String? = "",
    @SerializedName("district")
    var district: String? = "",
    @SerializedName("dob")
    var dob: String? = "",
    @SerializedName("email")
    var email: String? = "", // eng.yogesh.it@gmail.com
    @SerializedName("firstname")
    var firstname: String? = "", // Yogesh Sharma
    @SerializedName("gender")
    var gender: String? = "",
    @SerializedName("id")
    var id: Int? = 0, // 3081
    @SerializedName("institute")
    var institute: String? = null, // null
    @SerializedName("is_email_verify")
    var isEmailVerify: String? = "", // 0
    @SerializedName("is_mob_verify")
    var isMobVerify: String? = "", // 0
    @SerializedName("is_pro")
    var isPro: String? = "", // 0
    @SerializedName("lastname")
    var lastname: String? = null, // null
    @SerializedName("mobile")
    var mobile: String? = "",
    @SerializedName("photo")
    var photo: String? = "", // https://demo.com/logo.png
    @SerializedName("pin")
    var pin: String? = "",
    @SerializedName("state")
    var state: String? = "",
    @SerializedName("status")
    var status: String? = "", // 1
    @SerializedName("user_type")
    var userType: String? = "",
    @SerializedName("username")
    var username: String? = ""
): Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is UserModel) {
            false
        } else this.id == other.id
    }
}