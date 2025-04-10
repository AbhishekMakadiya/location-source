package com.location.location.models


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LicenseModel(
    @SerializedName("date_of_purchase")
    var dateOfPurchase: String = "",
    @SerializedName("days_left")
    var daysLeft: String = "",
    @SerializedName("domains_count")
    var domainsCount: Int = 0,
    @SerializedName("domains_data")
    var domainsData: List<DomainsData> = ArrayList(),
    @SerializedName("expiry_date")
    var expiryDate: String = "",
    @SerializedName("is_expired")
    var isExpired: Boolean = false,
    @SerializedName("license_key")
    var licenseKey: String = "",
    @SerializedName("license_type")
    var licenseType: String = "",
    @SerializedName("status")
    var status: String = ""
) : Serializable {
    data class DomainsData(
        @SerializedName("domain_id")
        var domainId: Int = 0,
        @SerializedName("registered_domain")
        var registeredDomain: String = ""
    ): Serializable
}