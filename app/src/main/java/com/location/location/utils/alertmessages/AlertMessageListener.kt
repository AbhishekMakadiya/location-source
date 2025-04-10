package com.location.location.utils.alertmessages

abstract class AlertMessageListener : OnAlertMessageListener {
    override fun onPositiveButtonClick() {

    }

    override fun onNegativeButtonClick() {

    }


}

interface OnAlertMessageListener {
    fun onPositiveButtonClick()
    fun onNegativeButtonClick()
}
