package com.location.location.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.view.View
import android.util.TypedValue
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LiveData
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.location.location.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.hideShow(isHide: Boolean) {
    visibility = View.GONE.takeIf { isHide } ?: View.VISIBLE
}

fun Context.hasPermission(permission: String): Boolean {

    return ActivityCompat.checkSelfPermission(this, permission) ==
            PackageManager.PERMISSION_GRANTED
}

fun Activity.requestPermissionWithRationale(
    permission: String,
    requestCode: Int,
    rationaleStr: String
) {
    val provideRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

    if (provideRationale) {
        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.app_name))
            setMessage(rationaleStr)
            setPositiveButton("Ok") { _, _ ->
                ActivityCompat.requestPermissions(this@requestPermissionWithRationale, arrayOf(permission), requestCode)
            }
            create()
            show()
        }
    } else {
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
    }
}
//if any issue occures in existing functionality uncomment below function same as in main activity
/*fun Context.checkNetwork(): CheckConnectivity {
    val connectivityManager: ConnectivityManager =
        this.applicationContext.getSystemService(Application.CONNECTIVITY_SERVICE) as ConnectivityManager
    return CheckConnectivity.getInstance(connectivityManager)
}*/
fun Context.checkNetwork(): LiveData<Boolean> {
    val connectivityManager: ConnectivityManager =
        this.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    return CheckConnectivity.getInstance(connectivityManager)//.networkStatus
}

fun sanitizePhoneNumber(phoneNumber: String): String {
    return phoneNumber.replace("[^\\d+]".toRegex(), "")
}

fun isFileAvailable(filePath: String?) : Boolean{
    if(!filePath.isNullOrEmpty()) {
        val file = filePath?.let { File(it) }
        return file?.exists() ?: false
    } else {
        return false
    }
}

 suspend fun Context.cacheImage(url: String?) {
     url?.let {
         withContext(Dispatchers.IO) {
             Glide.with(applicationContext)
                 .load(url)
                 .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                 .submit()
                 .get()
         }
     }
}

fun isServerUrl(mUrl: String?): Boolean {
    if (mUrl.isNullOrEmpty()) {
        return false
    } else {
        if (!mUrl.startsWith("http://") && !mUrl.startsWith("https://")) {
            return false
        } else {
            return true
        }
    }
}

fun Int.toSdp(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}

fun formatedDecimalValue(value: Double): String {
    return try {
        /*val formatted = String.format("%.2f", value)
        if (value % 1.0 == 0.0) value.toInt().toString() else formatted*/
        String.format("%.2f", value)
    } catch (e: Exception) {
        "0.0" // Handle any unexpected exceptions
    }
}