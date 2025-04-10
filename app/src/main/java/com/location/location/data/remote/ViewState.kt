package com.location.location.data.remote

import com.location.location.models.BaseResponse

enum class Status {
    INITIATE,
    SUCCESS,
    ERROR,
    LOADING
}

data class ViewState<out T>(val status: Status, val data: T?, val message: String?, val throwable: Throwable?) {

    companion object {

        fun <T> success(data: BaseResponse<T>?): ViewState<BaseResponse<T>> {
            return ViewState(Status.SUCCESS, data, null, null)
        }

        fun <T> error(msg: String): ViewState<T> {
            return ViewState(Status.ERROR, null, msg, null)
        }
        fun <T> error(throwable: Throwable? = null): ViewState<T> {
            return ViewState(Status.ERROR, null, null, throwable)
        }

        fun <T> loading(): ViewState<T> {
            return ViewState(Status.LOADING, null, null, null)
        }
    }

}