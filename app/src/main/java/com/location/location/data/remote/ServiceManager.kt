package com.location.location.data.remote

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.location.location.BuildConfig
import com.location.location.constants.Const.PARAM_ACCESS_TOKEN
import com.location.location.constants.Const.PARAM_DEVICE_TOKEN
import com.location.location.constants.Const.PARAM_USER_ID
import com.location.location.models.BaseResponse
import com.location.location.models.UserModel
import com.location.location.utils.PreferenceManager
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException
import java.security.cert.CertificateException
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


/**
 * service manager class is use for create retrofit client and create service request method
 *
 * in retrofit client add interceptor for network communication error and log interceptor
 * */
class ServiceManager(private val mContext: Context) {

    private val mPreferenceManager: PreferenceManager = PreferenceManager(mContext)

    private fun buildClient(): OkHttpClient {
        // this interceptor is for print MyResponse in log
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(ConnectivityInterceptor(mContext))
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(ConnectivityInterceptor(mContext))


        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(loggingInterceptor)

        // attach Header For Authentication
        if (!mPreferenceManager.getUserId().isNullOrEmpty()) {
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(
                        PARAM_ACCESS_TOKEN,
                        mPreferenceManager.getAccessToken()
                    )
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
        }

        return httpClient.build()
    }

    private val UTF8 = Charset.forName("UTF-8")
    private val commonResponse: Interceptor = object : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            var response: Response? = null
            try {
                val request = chain.request()
                response = chain.proceed(request)
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
                val tempResponse: BaseResponse<ResponseBody> = gson.fromJson(
                    buffer.clone().readString(charset),
                    BaseResponse<ResponseBody>()::class.java
                )

                /*
                * In API response we will not getting status code. so directly return response. instead of checking status code.
                * Note : Still we check this code and not return response directly, then api call twice (after first call response hit for second time).
                */
                return response

                //LogHelper.e("testing", tempResponse.code.toString() + "")
                /*if (tempResponse.isSuccess) {
                    return response
                } else if (tempResponse.code == Const.LOGOUT) {
                    if (mPreferenceManager.getUserLogin()) {
                        val intent = Intent(Const.ACTION_SESSION_EXPIRE)
                        intent.putExtra(Intent.EXTRA_TEXT, tempResponse.message)
                        mContext.sendBroadcast(intent)
                    }
                }*/
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
            return chain.proceed(chain.request())
        }
    }

    private fun buildClientForTinyUrl(): OkHttpClient {
        // this interceptor is for print MyResponse in log
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(ConnectivityInterceptor(mContext))
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            .addInterceptor(ConnectivityInterceptor(mContext))


        if (BuildConfig.DEBUG)
            httpClient.addInterceptor(loggingInterceptor)

        // attach Header For Authentication
        if (!mPreferenceManager.getUserId().isNullOrEmpty()) {
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(
                        PARAM_ACCESS_TOKEN,
                        mPreferenceManager.getAccessToken()
                    )
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
        }

        return httpClient.build()
    }

    private fun getUnsafeOkHttpClient(): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String,
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String,
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val builder = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .addInterceptor(ConnectivityInterceptor(mContext))
                .addInterceptor(CommonInterceptor(mContext))
                .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                .hostnameVerifier { _, _ -> true }


            // attach Header For Authentication
            //if (preferenceManager.getUserId().isNullOrEmpty()) {
            builder.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header(
                        PARAM_ACCESS_TOKEN,
                        mPreferenceManager.getAccessToken()
                    )
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            //}

            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun getOkHttpClient(): OkHttpClient {
        try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
                    return arrayOf()
                }
            })

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory


            val builder = OkHttpClient.Builder()
                .addInterceptor(ConnectivityInterceptor(mContext))
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.hostnameVerifier(HostnameVerifier { _, _ -> true })


            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            builder.addInterceptor(interceptor)

            builder.connectTimeout(30, TimeUnit.SECONDS)
            builder.readTimeout(30, TimeUnit.SECONDS)

            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun buildApi(): ApiServices {
        return Retrofit.Builder()
            .baseUrl(NetworkUtils.getUrl(Api.MAINURL))
            .client(getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }


    private fun addKey(params: HashMap<String, Any?>): HashMap<String, Any?> {
        params[PARAM_DEVICE_TOKEN] =
            mPreferenceManager.getFCMToken()
        params[PARAM_USER_ID] = mPreferenceManager.getUserId()
        return params
    }

    private fun addTwilioKey(params: HashMap<String, Any?>): HashMap<String, Any?> {
        params[PARAM_DEVICE_TOKEN] =
            mPreferenceManager.getFCMToken()
        return params
    }

    private fun addKeyMultipart(params: HashMap<String, RequestBody?>): HashMap<String, RequestBody?> {
        params[PARAM_DEVICE_TOKEN] =
            mPreferenceManager.getFCMToken()
                .toReqBody()
        params[PARAM_USER_ID] = mPreferenceManager.getUserId().toString().toReqBody()
        return params
    }


    /**
     *
     * Api For Login User
     *
     * */

    /*fun apiLogin(
        params: HashMap<String, Any?>,
        l: OnResponseListener<Response<LoginModel>>,
    ) {

        val call = buildApi().apiLogin(params)
        call.enqueue(object : Callback<Response<LoginModel>> {
            override fun onResponse(
                call: Call<Response<LoginModel>>,
                response: retrofit2.Response<Response<LoginModel>>,
            ) {
                val body = response.body()
                if (body != null) {
                    if (body.code == Const.SUCCESS)
                        l.onRequestSuccess(response.body()!!)
                    else
                        l.onRequestFailed(body.message)
                } else
                    l.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<Response<LoginModel>>, t: Throwable) {
                l.onRequestFailed(t)
            }
        })
    }*/


    /**
     *
     * Api for Logout User
     *
     * */

    /*fun apiLogout(
        params: HashMap<String, Any?>,
        l: OnResponseListener<Response<Any>>,
    ) {
        val call = buildApi().apiLogout(addKey(params))
        call.enqueue(object : Callback<Response<Any>> {
            override fun onResponse(
                call: Call<Response<Any>>,
                response: retrofit2.Response<Response<Any>>,
            ) {
                val body = response.body()
                if (body != null) {
                    if (body.code == Const.SUCCESS)
                        l.onRequestSuccess(response.body()!!)
                    else
                        l.onRequestFailed(body.message)
                } else
                    l.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<Response<Any>>, t: Throwable) {
                l.onRequestFailed(t)
            }
        })
    }*/

    /**
     *
     * Api For Update Profile
     *
     * */

    /*fun apiUpdateProfile(
        params: HashMap<String, RequestBody?>,
        files: ArrayList<MultipartBody.Part?>,
        l: OnResponseListener<Response<UserModel>>,
    ) {

        val call = buildApi().apiUpdateProfile(addKeyMultipart(params), files)
        call.enqueue(object : Callback<Response<UserModel>> {
            override fun onResponse(
                call: Call<Response<UserModel>>,
                response: retrofit2.Response<Response<UserModel>>,
            ) {
                val body = response.body()
                if (body != null) {
                    if (body.code == Const.SUCCESS)
                        l.onRequestSuccess(response.body()!!)
                    else if (body.code != Const.LOGOUT)
                        l.onRequestFailed(body.message)
                } else
                    l.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<Response<UserModel>>, t: Throwable) {
                l.onRequestFailed(t)
            }
        })
    }*/


    /**
     *
     * Api For Update Cover Photo
     *
     * */

    fun apiUploadMedia(
        params: HashMap<String, RequestBody?>,
        files: ArrayList<MultipartBody.Part?>,
        l: OnListener<BaseResponse<UserModel>>,
    ) {

        /*val call = buildApi().apiUploadMedia(addKeyMultipart(params), files)
        call.enqueue(object : Callback<BaseResponse<ChatMessageModel>> {
            override fun onResponse(
                call: Call<BaseResponse<ChatMessageModel>>,
                response: retrofit2.Response<BaseResponse<ChatMessageModel>>,
            ) {
                val body = response.body()
                if (body != null) {
                    if (body.code == Const.SUCCESS)
                        l.onRequestSuccess(response.body()!!)
                    else if (body.code != Const.LOGOUT)
                        l.onRequestFailed(body.message)
                    else if (body.code != Const.BLOCKED_BY_ADMIN)
                        l.onRequestFailed(body.message)
                } else
                    l.onRequestFailed(response.message())
            }

            override fun onFailure(call: Call<BaseResponse<ChatMessageModel>>, t: Throwable) {
                l.onRequestFailed(t)
            }
        })*/
    }
}