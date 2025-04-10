package com.location.location.models


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "PlaceTable")
data class PlaceModel(
    @PrimaryKey(autoGenerate = true)
    var placeId: Int? = null,
    @SerializedName("name")
    var name: String? = "",
    @SerializedName("address")
    var address: String? = "",
    @SerializedName("latitude")
    var latitude: Double? =0.0,
    @SerializedName("longitude")
    var longitude: Double? =0.0,
    var isPrimary: Int = 0 //  0 for false, 1 for true
) : Serializable
