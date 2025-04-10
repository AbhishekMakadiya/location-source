package com.location.location.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SimulationModel(
    @SerializedName("author")
    var author: String? = "", // SimPHY
    @SerializedName("currency")
    var currency: String? = "", // USD
    @SerializedName("currency_symbol")
    var currencySymbol: String? = "", // $
    @SerializedName("id")
    var id: Int? = 0, // 129
    @SerializedName("is_favorite")
    var isFavorite: Boolean? = false, // true
    @SerializedName("is_download")
    var isDownload: Boolean? = false, // true
    @SerializedName("is_free")
    var isFree: Boolean? = false, // false
    @SerializedName("is_view")
    var isView: Boolean? = false, // false
    @SerializedName("long_description")
    var longDescription: String? = "",
    @SerializedName("download_url")
    var downloadUrl: String? = "",
    @SerializedName("photo")
    var photo: String? = "", // https://simulation.simphy.com/data/wbIn226GNtSCkO9hxmRvJdVH3gq4DfPLY435Z5UjucK70yQ08X.png
    @SerializedName("price")
    var price: String? = "0", // 5
    @SerializedName("order_id")
    var orderId: String? = "", // 5
    @SerializedName("order_date")
    var orderDate: String? = "",
    @SerializedName("order_status")
    var orderStatus: String? = "",
    @SerializedName("share")
    var share: String? = "", // Hey! I found a 'Velocity of Approach' simulation on Simphy. Check it out here: https://simphy.com/view?velocity-of-approach-0526&129
    @SerializedName("short_description")
    var shortDescription: String? = "", // Three particles A, B, and C are situated at the vertices of an equilateral triangle ABC of side d at time t = 0. Each of the particles moves with constant speed v. A always has its velocity along AB, B along BC, and C along CA. At what time will the particles meet each other?
    @SerializedName("slug")
    var slug: String? = "", // velocity-of-approach-0526
    @SerializedName("title")
    var title: String? = "", // Velocity of Approach
    @SerializedName("url")
    var url: String? = "",
    @SerializedName("web_url")
    var webUrl: String? = "",
    @SerializedName("version")
    var version: String? = "" // SimPHY 2.9
): Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other == null || other !is SimulationModel) {
            false
        } else this.id == other.id
    }
}