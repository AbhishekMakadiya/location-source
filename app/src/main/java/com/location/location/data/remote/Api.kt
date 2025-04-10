package com.location.location.data.remote

object

Api {
    val MAINURL = APIURL.STAGING

    const val SCHEME = "https"
    const val AUTHORITY_STAGING = "api.simphy.com"
    const val AUTHORITY_PRODUCTION = "api.simphy.com"
    const val SOCKET_AUTHORITY_PRODUCTION = "api.simphy.com"
    const val SOCKET_AUTHORITY_STAGING = "api.simphy.com"

    //    const val PATH = "api/"
    const val PATH = ""


    const val FILE_PATH = "uploads/points_attachment/"

    const val API_USER_REGISTER = "registration"

}

enum class APIURL {
    STAGING, PRODUCTION
}

