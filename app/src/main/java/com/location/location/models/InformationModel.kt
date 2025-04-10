package com.location.location.models


import com.google.gson.annotations.SerializedName

data class InformationModel(
    @SerializedName("contact")
    var contact: List<String> = ArrayList(),
    @SerializedName("email")
    var email: String = "",
    @SerializedName("pages")
    var pages: Pages = Pages()
) {
    data class Pages(
        @SerializedName("my_plan")
        var myPlan: String = "",
        @SerializedName("privacy")
        var privacy: String = "",
        @SerializedName("term")
        var term: String = "",
    )
}