package com.location.location.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HomeCategoryModel(
        @SerializedName("simulations")
        var simulations: ArrayList<SimulationModel>? = null,
        @SerializedName("title")
        var title: String = "" // Newly Added
    ): Serializable