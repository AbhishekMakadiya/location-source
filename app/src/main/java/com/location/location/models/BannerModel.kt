package com.location.location.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BannerModel(
    @SerializedName("photo")
    var photo: String? = "", // https://picsum.photos/384/180
    @SerializedName("title")
    var title: String? = "" // Love With Physics
): Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is BannerModel) {
            false
        } else this.photo == other.photo
    }
}