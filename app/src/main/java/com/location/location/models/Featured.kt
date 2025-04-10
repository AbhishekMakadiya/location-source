package com.location.location.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FeaturedModel(
        @SerializedName("data")
        var data: ArrayList<FeaturedDataModel?>? = null,
        @SerializedName("title")
        var title: String = "" // More to Explore
    ): java.io.Serializable {
        data class FeaturedDataModel(
            @SerializedName("discription")
            var discription: String = "", // Create and simulate DC and AC circuit diagrams with basic circuit elements.
            @SerializedName("photo")
            var photo: String = "", // https://simulation.simphy.com/data/ut7VsMPRxJ826g3GDInQypvEHe9T1wh6SOF07dNL0iqUojmX4W.webp
            @SerializedName("title")
            var title: String = "", // Circuit Simulator
            @SerializedName("url")
            var url: String = "" // topics=21-6&type=&author=&tags=&page=1
        ): Serializable
    }