package com.location.location.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FiltersModel(
    @SerializedName("author")
    var author: ArrayList<AuthorModel?>? = null,
    @SerializedName("topics")
    var topics: ArrayList<TopicModel?>? = null,
    @SerializedName("type")
    var type: ArrayList<TypeModel?>? = null
): Serializable {
    data class AuthorModel(
        @SerializedName("id")
        var id: String = "", // Simphy
        @SerializedName("title")
        var title: String = "", // Simphy
        var isSelected: Boolean = false // Added isSelected field
    ):Serializable {
        override fun equals(other: Any?): Boolean {
            return if (other == null || other !is AuthorModel) {
                false
            } else this.id == other.id
        }
    }

    data class TopicModel(
        @SerializedName("id")
        var id: Int = 0, // 21
        @SerializedName("title")
        var title: String = "", // Current Electricity
        var isSelected: Boolean = false // Added isSelected field
    ):Serializable {
        override fun equals(other: Any?): Boolean {
            return if (other == null || other !is TopicModel) {
                false
            } else this.id == other.id
        }
    }

    data class TypeModel(
        @SerializedName("id")
        var id: String = "", // Free
        @SerializedName("title")
        var title: String = "", // Free
        var isSelected: Boolean = false // Added isSelected field
    ):Serializable {
        override fun equals(other: Any?): Boolean {
            return if (other == null || other !is TypeModel) {
                false
            } else this.id == other.id
        }
    }
}