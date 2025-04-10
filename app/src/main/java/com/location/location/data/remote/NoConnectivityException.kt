package com.location.location.data.remote

import java.io.IOException

class NoConnectivityException : IOException() {

    override fun getLocalizedMessage(): String {
        return "No connectivity exception"
    }
}