package com.location.location.data.remote

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.webkit.MimeTypeMap
import android.widget.EditText
import com.location.location.R
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import java.text.ParseException
import java.util.concurrent.TimeoutException

object NetworkUtils {

    fun getUrl(URL: APIURL): String {
        val builder = Uri.Builder()
        builder.scheme(Api.SCHEME)
        when (URL) {
            APIURL.STAGING -> builder.encodedAuthority(Api.AUTHORITY_STAGING)
            APIURL.PRODUCTION -> builder.encodedAuthority(Api.AUTHORITY_PRODUCTION)
        }
        builder.appendEncodedPath(Api.PATH)
        return builder.build().toString()
    }

    fun getSocketUrl(URL: APIURL): String {
        val builder = Uri.Builder()
        builder.scheme(Api.SCHEME)
        when (URL) {
            APIURL.STAGING -> builder.encodedAuthority(Api.SOCKET_AUTHORITY_STAGING)
            APIURL.PRODUCTION -> builder.encodedAuthority(Api.SOCKET_AUTHORITY_PRODUCTION)
        }
        /*when (URL) {
            APIURL.STAGING -> builder.appendEncodedPath(Api.API_STAGING_TOKEN)
            APIURL.PRODUCTION -> builder.appendEncodedPath(Api.API_PRODUCTION_TOKEN)
        }*/
        return builder.build().toString()
    }

    fun getFileUrl(URL: APIURL): String {
        val builder = Uri.Builder()
        builder.scheme(Api.SCHEME)
        when (URL) {
            APIURL.STAGING -> builder.encodedAuthority(Api.AUTHORITY_STAGING)
            APIURL.PRODUCTION -> builder.encodedAuthority(Api.AUTHORITY_PRODUCTION)
        }
        builder.appendEncodedPath(Api.FILE_PATH)
        return builder.build().toString()
    }


    /**
     * check isInternetConnected
     * */
    fun isConnectedToInternet(mContext: Context?): Boolean {
        return try {
            val cm =
                mContext?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            val activeNetwork: NetworkInfo? = cm?.activeNetworkInfo
            activeNetwork?.isConnectedOrConnecting == true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * handle error msg
     * */
    fun getErrorMessage(mContext: Context, error: Throwable): String {
        return when (error) {
            is NoConnectivityException -> mContext.getString(R.string.error_no_internet)
            is UnknownHostException -> mContext.getString(R.string.request_error_server)
            is ConnectException -> mContext.getString(R.string.error_connect)
            is TimeoutException -> mContext.getString(R.string.request_error_time_out)
            is IOException -> mContext.getString(R.string.request_error_server)
            is IllegalStateException -> mContext.getString(R.string.request_error_network)
            is ParseException -> mContext.getString(R.string.request_error_parse)
            else -> error.message.takeIf { error.message != null }
                ?: mContext.getString(R.string.something_want_wrong)
        }
    }

    /**
     * prepare file part from local url
     */
    fun prepareFilePart(name: String, filePath: String?): MultipartBody.Part? {
        if (!filePath.isNullOrEmpty()) {
            val file = File(filePath)

            var fileName = file.name.replace("[^A-Za-z0-9.]".toRegex(), "")
            val verifyFileName = fileName.substring(0, fileName.lastIndexOf('.'))
            if (verifyFileName.isEmpty()) {
                //here we get last index of . to get name of file.
                //e.g test.jpg then we get only test
                //So se there is oincanly special character in file name then we attache key name and set file name so that file name not gets invalid.
                //e.g. for #.jpg we set here key_name+.jpg
                fileName = name + fileName
            }
            return MultipartBody.Part.createFormData(
                name,
                fileName, file.asRequestBody(getMimeType(filePath.toString())?.toMediaTypeOrNull())
            )

        } else
            return null

    }

    /**
     * get mime type from url
     */
    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}

fun String.toReqBody(): RequestBody {
    return RequestBody.create(okhttp3.MultipartBody.FORM, this.trim())
}

fun EditText.toReqBody(): RequestBody {
    return this.text.toString().trim().toReqBody()
}