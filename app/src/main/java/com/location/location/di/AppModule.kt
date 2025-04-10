package com.location.location.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.location.location.BuildConfig
import com.location.location.constants.Const.PARAM_ACCEPT
import com.location.location.constants.Const.PARAM_ACCESS_TOKEN
import com.location.location.data.remote.Api
import com.location.location.data.remote.ApiHelper
import com.location.location.data.remote.ApiServiceImpl
import com.location.location.data.remote.ApiServices
import com.location.location.data.remote.CommonInterceptor
import com.location.location.data.remote.ConnectivityInterceptor
import com.location.location.data.remote.NetworkUtils
import com.location.location.utils.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    lateinit var mContext: Context

    private val httpLoggingInterceptor by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val connectivityInterceptor by lazy {
        ConnectivityInterceptor(mContext)
    }
    private val commonInterceptor by lazy {
        CommonInterceptor(mContext)
    }
    private val mPreferenceManager by lazy {
        PreferenceManager(mContext)
    }

    private val okHttpClient: OkHttpClient by lazy {
        val builder =OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.MINUTES)
            .readTimeout(5, TimeUnit.MINUTES)
            .writeTimeout(5, TimeUnit.MINUTES)
            //.
            .addInterceptor(connectivityInterceptor)
            .addInterceptor(commonInterceptor)
            .addInterceptor { chain ->
                /*val original = chain.request()
                val request = original.newBuilder()
                    .header(
                        PARAM_AUTHORIZATION,
                        "Bearer ${mPreferenceManager.getUserLoginData()?.accessToken}"
                    )
                    .header(PARAM_ACCEPT, "application/json")
                    .method(original.method, original.body)
                    .build()
                chain.proceed(request)*/
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header(PARAM_ACCEPT, "application/json")

                if (mPreferenceManager.getUserLogin()) { // Check if user is logged in
                    requestBuilder.header(PARAM_ACCESS_TOKEN, mPreferenceManager.getAccessToken())
                }

                val request = requestBuilder.method(original.method, original.body).build()
                chain.proceed(request)
            }
            if (BuildConfig.DEBUG){
                builder.addInterceptor(httpLoggingInterceptor) // Crash out of memory while uploading multipart files because of large log size
            }
            builder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit {
        mContext=context
       return Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(NetworkUtils.getUrl(Api.MAINURL))
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiServices =
        retrofit.create(ApiServices::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiServiceImpl): ApiHelper = apiHelper

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun providePreferenceManager(@ApplicationContext context: Context): PreferenceManager {
        return PreferenceManager(context)
    }
    @Provides
    @Singleton
    fun provideSocket(mPreferenceManager: PreferenceManager): Socket {

        if (mPreferenceManager.getUserLogin()){
            val opts = IO.Options()
            opts.transports = arrayOf(WebSocket.NAME)
            opts.query = "userId=${mPreferenceManager.getUserId()}"
            // Add headers to the options (using listOf() to create single-element lists)
            opts.extraHeaders = mutableMapOf(
                PARAM_ACCEPT to listOf("application/json"),
                PARAM_ACCESS_TOKEN to listOf(mPreferenceManager.getAccessToken())
            )
            return IO.socket(NetworkUtils.getSocketUrl(Api.MAINURL), opts)
        }else{
            // Provide a default socket to avoid uninitialized property access.
            return IO.socket(NetworkUtils.getSocketUrl(Api.MAINURL))
        }
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext appContext: Context): Context {
        return appContext
    }
}