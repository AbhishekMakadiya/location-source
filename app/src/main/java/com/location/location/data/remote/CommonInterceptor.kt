package com.location.location.data.remote

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.location.location.constants.Const
import com.location.location.models.BaseResponse
import okhttp3.Interceptor
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

class CommonInterceptor(private val mContext: Context) : Interceptor {
    private val UTF8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response: okhttp3.Response?
        try {
            val request = chain.request()
            response = chain.proceed(request)

            if (response.code == 401) {
                // Intercept the response body
                val responseBody = response.body
                val responseBodyBuffer = Buffer()
                responseBody?.source()?.readAll(responseBodyBuffer)

                // Do something with the response body buffer
                val responseBodyString = responseBodyBuffer.readUtf8()
                var message: String
                responseBodyString.let {
                    val jsonObject = JSONObject(it)
                    message = jsonObject.optString("message")
                }
                val intent = Intent(Const.ACTION_SESSION_EXPIRE)
                intent.putExtra(Intent.EXTRA_TEXT, message)
                mContext.sendBroadcast(intent)
                return response
            }

            val responseBody = response.body
            val source = responseBody!!.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer
            var charset: Charset = UTF8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8)!!
                } catch (e: UnsupportedCharsetException) {
                    return response
                }
            }
            val gson = Gson()
            val tempBaseResponse: BaseResponse<ResponseBody>? = gson.fromJson(
                buffer.clone().readString(charset),
                BaseResponse<ResponseBody>()::class.java
            )
            if (tempBaseResponse?.isLogin == false){
                val intent = Intent(Const.ACTION_SESSION_EXPIRE)
                intent.putExtra(Intent.EXTRA_TEXT, tempBaseResponse?.message)
                mContext.sendBroadcast(intent)
                return response
            }

            /*
            * In API response we will not getting status code. so directly return response. instead of checking status code.
            * Note : Still we check this code and not return response directly, then api call twice (after first call response hit for second time).
            */
            return response

            //LogHelper.e("testing", tempBaseResponse.code.toString() + "")
            /*when (tempBaseResponse?.code) {
                Const.LOGOUT -> {
                    //if (mPreferenceManager.getUserLogin()) {
                    //val intent = Intent(Const.ACTION_SESSION_EXPIRE)
                    //intent.putExtra(Intent.EXTRA_TEXT, tempResponse.message)
                    //mContext?.sendBroadcast(intent)
                    //}
                }

                Const.USER_DELETED -> {
                    val intent = Intent(Const.ACTION_USER_DELETED)
                    intent.putExtra(Intent.EXTRA_TEXT, tempBaseResponse.message)
                    mContext.sendBroadcast(intent)
                    *//*AlertMessage.showMessage(
                            mContext,
                            response.message,
                            mContext!!.getString(R.string.ok),
                            object : AlertMessageListener(){
                                override fun onPositiveButtonClick() {
                                    super.onPositiveButtonClick()
                                    val intent = Intent(Const.ACTION_LOGOUT)
                                    intent.putExtra(Intent.EXTRA_TEXT, "")
                                    mContext?.sendBroadcast(intent)
                                }
                            }
                        )*//*
                }

                Const.SUCCESS -> {
                    return response
                }
            }*/
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return chain.proceed(chain.request())
    }
}