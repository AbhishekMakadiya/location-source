package com.location.location.imagepicker2

interface ImagePickResult {
    fun onSuccess(imagePicRequestResultData: ImagePicRequestResultData?)
    fun onFail(message: String?)
}