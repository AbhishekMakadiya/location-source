package com.location.location.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeResponseModel(
    @SerializedName("featured")
    var featured: FeaturedModel = FeaturedModel(),
    @SerializedName("filters")
    var filters: FiltersModel? = null,
    @SerializedName("home_category")
    var homeCategory: ArrayList<HomeCategoryModel?>? = null,
    @SerializedName("slider")
    var slider: ArrayList<BannerModel?>? = null,
    @SerializedName("user")
    var user: UserModel? = null,
    @SerializedName("information")
    var information: InformationModel? = null
): Serializable {

}