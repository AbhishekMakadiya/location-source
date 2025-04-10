package com.location.location.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.*
import android.content.Context.CLIPBOARD_SERVICE
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.inputmethod.InputMethodManager
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.location.location.fcmNotification.FcmTokenValidator
import com.location.location.utils.alertmessages.AlertMessage
import com.location.location.views.MyCustomEditText
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.tabs.TabLayout
import com.google.firebase.messaging.FirebaseMessaging
import com.location.location.R
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern


object Util {


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
     * show toast messages
     */
    fun message(mContext: Context?, message: String?) {
        if (mContext != null && message != null && !message.trim().isEmpty()) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * show toast messages
     */
    fun showMessage(title: String, message: String, mContext: Context) {
        if (message.trim().isNotEmpty()) {
            AlertMessage.showMessage(mContext, message, title)
        }
    }

    /**
     * show toast messages
     */
    fun showMessage(mContext: Context?, message: String?) {
        if (mContext != null && message != null && !message.trim().isEmpty()) {
            AlertMessage.showMessage(mContext, message)
        }
    }

    /**
     * Hide Keyboard
     */
    fun hideKeyboard(mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        try {
            imm.hideSoftInputFromWindow((mContext as Activity).window.currentFocus?.windowToken, 0)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    fun hideKeyboard(dialog: Dialog?) {
        try {
            if (dialog != null) {
                if (dialog.window != null) {
                    val imm: InputMethodManager =
                        dialog.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(dialog.window?.currentFocus?.windowToken, 0)
                }
            }
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    /**
     * show keyboard
     * */
    fun showKeyboard(mContext: Context) {
        try {
            val inputMethodManager =
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.toggleSoftInputFromWindow(
                (mContext as Activity).window.currentFocus?.windowToken,
                InputMethodManager.SHOW_FORCED,
                0
            )
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }


    /**
     * load image resource
     * */
    fun loadImageResource(mContext: Context, imageResourceId: Int, imageView: ImageView) {
        Glide.with(mContext).load(imageResourceId).into(imageView)
    }


    /**
     * load image bitmap
     * */
    fun loadImageBitmap(
        mContext: Context,
        bitmap: Bitmap?,
        placeHolder: Int,
        imageView: ImageView,
    ) {
        Glide.with(mContext).load(bitmap)
            .apply(RequestOptions().placeholder(placeHolder).error(placeHolder))
            .into(imageView)
    }

    /**
     * load local image
     * */
    fun loadLocalImage(
        mContext: Context,
        imageUrl: String?,
        placeHolder: Int,
        imageView: ImageView,
    ) {
        try {
            val requestOptions = RequestOptions()
                .placeholder(placeHolder)
                .signature(ObjectKey(File(imageUrl!!).lastModified()))
                .error(placeHolder)
                .dontAnimate()

            Glide.with(mContext).load(imageUrl)
                .apply(requestOptions)
                .into(imageView)
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    fun loadBackgroundPictures(
        mContext: Context,
        file: File,
        imageView: ImageView,
        placeHolder: Int
    ) {
        try {
            val requestOptions = RequestOptions()
                .signature(ObjectKey(file.lastModified()))
                .placeholder(placeHolder)

            Glide.with(mContext)
                .load(file)
                .apply(requestOptions)
                .into(imageView)
        } catch (e: Exception) {
            //            You cannot start a load for a destroyed activity
            //            LogHelper.printStack(e);
        }

    }

    fun loadImageUrl(
        mContext: Context,
        imageUrl: String?,
        placeHolder: Int?,
        imageView: ImageView,
    ) {
        try {
            if (placeHolder != null) {
                val requestOptions = RequestOptions()
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .dontAnimate()

                Glide.with(mContext).load(imageUrl)
                    .apply(requestOptions)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(imageView)
            } else {
                Glide.with(mContext).load(imageUrl)
                    .into(imageView)
            }

        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    fun loadImageUrlBlurPlaceholder(
        mContext: Context,
        imageUrl: String?,
        placeHolder: Int?,
        imageView: ImageView,
    ) {
        try {
            if (placeHolder != null) {
                val requestOptions = RequestOptions()
                    .placeholder(placeHolder)
                    .error(placeHolder)
                    .dontAnimate()

                Glide.with(mContext).load(imageUrl)
                    .apply(requestOptions)
                    .thumbnail(0.01f)
                    .into(imageView)
            } else {
                Glide.with(mContext).load(imageUrl)
                    .thumbnail(0.01f)
                    .into(imageView)
            }

        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    /*fun addAnalyticsEvent(mContext: Context, eventName: String, map: HashMap<String, Any>) {
        map[AnalyticsEventConstant.DEVICE_ID] = getSecureKey(mContext).toString()
        map[AnalyticsEventConstant.DEVICE_NAME] = Build.MODEL
        val analyticsEvent = AnalyticsEvent(eventName, map)
        AnalyticsBaseController.getInstance(mContext).trackEvent(analyticsEvent)
    }*/
    fun toMilliSeconds(day: Double): Long {
        return (day * 24 * 60 * 60 * 1000).toLong()
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    fun isNUmberValid(mString: String): Boolean {
        val expression = "^\\d*(\\.\\d+)?\$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(mString)
        return matcher.matches()
    }

    private fun getUrlWithHeaders(url: String, token: String): GlideUrl {
        return GlideUrl(
            url, LazyHeaders.Builder()
                .addHeader("Authorization", token)
                .build()
        )
    }

    fun changeTabsFont(mContext: Context, tabLayout: TabLayout) {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            changeTabsMargin(vgTab, tabLayout)
            val tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    //tabViewChild.typeface = Typeface.createFromAsset(mContext.assets, Const.FONT_PATH_SEMI_BOLD)
                    tabViewChild.isAllCaps = false
                    tabViewChild.textSize = mContext.resources.getDimension(R.dimen.txt_16sp)
                }
            }
        }
    }

    fun changeTabsFont2(mContext: Context, tabLayout: TabLayout) {
        val vg = tabLayout.getChildAt(0) as ViewGroup
        val tabsCount = vg.childCount
        for (j in 0 until tabsCount) {
            val vgTab = vg.getChildAt(j) as ViewGroup
            changeTabsMargin(vgTab, tabLayout)
            val tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                val tabViewChild = vgTab.getChildAt(i)
                tabViewChild.setPadding(0, 0, 0, 0)
                if (tabViewChild is TextView) {
                    //tabViewChild.typeface = Typeface.createFromAsset(mContext.assets, Const.FONT_PATH_BOLD)
                    tabViewChild.isAllCaps = true
                    tabViewChild.includeFontPadding = false
                    tabViewChild.textSize = mContext.resources.getDimension(R.dimen.txt_14sp)
                }
            }
        }
    }

    private fun changeTabsMargin(childAt: ViewGroup, tabLayout: TabLayout) {
        val layoutParams = childAt.layoutParams as LinearLayout.LayoutParams
        layoutParams.weight = 0f
        layoutParams.marginEnd = 12
        layoutParams.topMargin = 15
        layoutParams.bottomMargin = 15
        layoutParams.marginStart = 12
        childAt.layoutParams = layoutParams
        tabLayout.requestLayout()
    }

    @SuppressLint("HardwareIds")
    fun getSecureKey(mContext: Context): String? {
        return Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /*
    * Spannable two string
    * */
    fun SpanTwoString(
        txtFirst: String,
        txtSecond: String,
        firstTxtColor: Int,
        secondTxtColor: Int,
    ): SpannableStringBuilder {
        val span1 = SpannableStringBuilder(txtFirst)
        span1.setSpan(
            ForegroundColorSpan(firstTxtColor),
            0,
            span1.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        val span2 = SpannableStringBuilder(txtSecond)
        span2.setSpan(
            ForegroundColorSpan(secondTxtColor),
            0,
            span2.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )

        /*val span3 = SpannableStringBuilder(txtSeprator)
        span3.setSpan(ForegroundColorSpan(firstTxtColor), 0, span3.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)*/

        //val concatenated: Spanned = TextUtils.concat(span1, span3, span2) as Spanned
        val concatenated: Spanned = TextUtils.concat(span1, " ", span2) as Spanned
        val result = SpannableStringBuilder(concatenated)
        return result
    }

    /**
     * show progress dialog
     */
    @Suppress("DEPRECATION")
    fun getProgressDialog(mContext: Context): ProgressDialog {
        val dialog = ProgressDialog(mContext)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.isIndeterminate = true
        dialog.setMessage(mContext.getString(R.string.please_wait))
        return dialog
    }

    fun createPartFromString(descriptionString: String): RequestBody {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString)
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

    private fun getTokenProgressDialog(mContext: Context): Dialog {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.progress_view_layout)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        val window = dialog.window
        window?.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        return dialog
    }


    /**
     * This method will check for fcm token. If not received then it try to get it.
     * If not getting token then we will notify it by method with reason.
     * If we get token then it will notify it by passing token.
     */
    fun validateTokenAndCallAPI(
        @NonNull mContext: Context, fcmTokenValidator: FcmTokenValidator?,
        isShowProgressDialog: Boolean = true, @NonNull requestCode: Int,
    ) {
        if (!isConnectedToInternet(mContext)) {
            fcmTokenValidator?.onFCMTokenRetrievalFailed(mContext.getString(R.string.error_no_internet))
            return
        }
        val preferenceManager = PreferenceManager(mContext)
        val storedFCMToken = preferenceManager.getFCMToken()
        if (storedFCMToken.isNullOrEmpty()) {
            LogHelper.e(
                "validateTokenAndCallAPI",
                "token not available in local, so try to get new token"
            )
            //when we are fetching token then show dialog, so that user aware of this.
            val tokenProgressDialog = getTokenProgressDialog(mContext)

            if (isShowProgressDialog)
                tokenProgressDialog.show()

            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                //When we get response of then hide progress dialog and change its message.Because this dialog is used for showing login api progress.
                //So in login api call we show message for token that is not valid. So simply change message and hide dialog.

                if (isShowProgressDialog)
                    tokenProgressDialog.dismiss()

                if (task.isSuccessful && task.result != null && task.result.isNotEmpty()) {
                    LogHelper.e("validateTokenAndCallAPI", "getting token here, not from receiver")
                    preferenceManager.setFCMToken(
                        task.result
                    )

                    fcmTokenValidator?.onFCMTokenReceived(task.result, requestCode)
                } else {
                    val exception = task.exception
                    exception?.printStackTrace()
                    if (exception?.message != null) {
                        //If we have any error message then we will directly display that to user.
                        fcmTokenValidator?.onFCMTokenRetrievalFailed(exception.message!!)
                    } else {
                        //if we didn't have any proper message then simply say that we are failed to fetch token.
                        fcmTokenValidator?.onFCMTokenRetrievalFailed(mContext.getString(R.string.something_want_wrong))
                    }
                }
            }
        } else {
            //If token available then directly notify it.
            fcmTokenValidator?.onFCMTokenReceived(storedFCMToken, requestCode)
        }

    }


    fun validateTokenAndCallAPI(
        @NonNull mContext: Context,
        @NonNull fcmTokenValidator: FcmTokenValidator,
        @NonNull requestCode: Int,
    ) {
        if (!isConnectedToInternet(mContext)) {
            fcmTokenValidator.onFCMTokenRetrievalFailed(mContext.getString(R.string.error_no_internet))
            return
        }
        val preferenceManager = PreferenceManager(mContext)
        val storedFCMToken = preferenceManager.getFCMToken()
        if (storedFCMToken.isNullOrEmpty()) {
            LogHelper.e(
                "validateTokenAndCallAPI",
                "token not available in local, so try to get new token"
            )
            //when we are fetching token then show dialog, so that user aware of this.
            val tokenProgressDialog = getTokenProgressDialog(mContext)
            //            progressDialog.show()
            tokenProgressDialog.show()
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                //When we get response of then hide progress dialog and change its message.Because this dialog is used for showing login api progress.
                //So in login api call we show message for token that is not valid. So simply change message and hide dialog.
                //                progressDialog.hide()
                tokenProgressDialog.dismiss()
                if (task.isSuccessful && task.result != null && task.result.isNotEmpty()) {
                    LogHelper.e("validateTokenAndCallAPI", "getting token here, not from receiver")
                    preferenceManager.setFCMToken(
                        task.result
                    )

                    fcmTokenValidator.onFCMTokenReceived(task.result, requestCode)
                } else {
                    val exception = task.exception
                    exception?.printStackTrace()
                    if (exception?.message != null) {
                        //If we have any error message then we will directly display that to user.
                        fcmTokenValidator.onFCMTokenRetrievalFailed(exception.message!!)
                    } else {
                        //if we didn't have any proper message then simply say that we are failed to fetch token.
                        fcmTokenValidator.onFCMTokenRetrievalFailed(mContext.getString(R.string.something_want_wrong))
                    }
                }
            }
        } else {
            //If token available then directly notify it.
            fcmTokenValidator.onFCMTokenReceived(storedFCMToken, requestCode)
        }

    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display ring1 dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private const val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    fun checkPlayServices(activity: Activity): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(
                    activity,
                    resultCode,
                    PLAY_SERVICES_RESOLUTION_REQUEST
                )
                    ?.show()
            } else {
                LogHelper.e("MyTagUtil.java", "This device is not supported.")
                //                activity.finish();
            }
            return false
        }
        return true
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
    }

    /*
* Start with rating the app
* Determine if the Play Store is installed on the device
*
* */
    fun rateApp(context: Context) {
        try {
            val rateIntent = rateIntentForUrl("market://details", context)
            context.startActivity(rateIntent)
        } catch (e: ActivityNotFoundException) {
            val rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", context)
            context.startActivity(rateIntent)
        }

    }

    private fun rateIntentForUrl(url: String, context: Context): Intent {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse(String.format("%s?id=%s", url, context.packageName))
        )
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, "com.cricbuzz.android")))
        var flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        if (Build.VERSION.SDK_INT >= 21) {
            flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
        } else {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                flags = flags or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                flags = flags or Intent.FLAG_ACTIVITY_NEW_DOCUMENT
            }
        }
        intent.addFlags(flags)
        return intent
    }

    //-- To get file path
    fun getPath(context: Context?, uri: Uri): String? {
        val isKitKatOrAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
        // DocumentProvider
        if (isKitKatOrAbove && DocumentsContract.isDocumentUri(
                context,
                uri
            )
        ) { // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context!!, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1]) as Array<String?>?
                return getDataColumn(
                    context!!,
                    contentUri,
                    selection,
                    selectionArgs
                )
            }
        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
            return getDataColumn(context!!, uri, null, null)
        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
            return uri.path
        }
        return null
    }


    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String?>?,
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /* get facebook keyhase sha-1*/
    @SuppressLint("PackageManagerGetSignatures")
    fun printKeyHash(context: Activity): String? {
        val packageInfo: PackageInfo
        var key: String? = null
        try {
            //getting application package name, as defined in manifest
            val packageName = context.applicationContext.packageName
            //Retriving package info
            packageInfo =
                context.packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)

            LogHelper.e("Package Name=", context.applicationContext.packageName)
            packageInfo.signatures?.let {
                for (signature in it) {
                    val md = MessageDigest.getInstance("SHA")
                    md.update(signature.toByteArray())
                    key = String(Base64.encode(md.digest(), 0))
                    key?.let {key1->
                        LogHelper.e("Key Hash=", key1)
                    }
                }
            }
        } catch (e1: PackageManager.NameNotFoundException) {
            LogHelper.e("Name not found", e1.toString())
        } catch (e: NoSuchAlgorithmException) {
            LogHelper.e("No such an algorithm", e.toString())
        } catch (e: Exception) {
            LogHelper.e("Exception", e.toString())
        }
        return key
    }

    /**
     *
     *      Validation for Alpha Numeric
     * */

    fun isAlphaNumeric(s: String): Boolean {
        val pattern = "^[a-zA-Z0-9]*$"
        return s.matches(pattern.toRegex())
    }

    /**
     *
     *      Validation for Valid Email
     * */

    fun isValidEmailId(email: String): Boolean {

        return Pattern.compile(
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$"
        ).matcher(email).matches()
    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    fun isEmailValid(email: String): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    /**
     *
     *  Create temp file from bitmap and return it
     *
     * */

    fun convertBitmapToFile(mContext: Context, bitmapImage: Bitmap): File {
        //create a file to write bitmap data
        val f = File(mContext.externalCacheDir, "video_thumbnail.jpeg")
        f.createNewFile()

        //Convert bitmap to byte array
        val bitmap = bitmapImage
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()

        //write the bytes in file
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        return f
    }

    fun getRandomNumber(min: Int, max: Int): Int {
        return Random().nextInt(max - min + 1) + min
    }

    fun openBookCab(
        mContext: Context,
        currLat: Double?,
        currLng: Double?,
        dropLat: Double?,
        dropLng: Double?,
    ) {
        val mIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://olawebcdn.com/assets/ola-universal-link.html?lat=$currLat&lng=$currLng&category=share&utm_source=xapp_token&landing_page=bk&drop_lat=$dropLat&drop_lng=$dropLng&affiliate_uid=12345")
        )
        mContext.startActivity(mIntent)
    }

    fun share(mContext: Context, mTitle: String, content: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TITLE, mTitle)
        shareIntent.putExtra(Intent.EXTRA_TEXT, content)
        mContext.startActivity(
            Intent.createChooser(
                shareIntent,
                mContext.getString(R.string.send_to)
            )
        )
    }

    fun getExtention(url: String): String {
        return url.substring(url.lastIndexOf("."))
    }

    fun getDirection1(mContext: Context, mSiteName: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$mSiteName, Ahmedabad, Gujarat, India")
        )
        intent.setPackage("com.google.android.apps.maps")
        try {
            mContext.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            try {
                val unrestrictedIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=$mSiteName, Ahmedabad, Gujarat, India")
                )
                mContext.startActivity(unrestrictedIntent)
            } catch (innerEx: ActivityNotFoundException) {
                showMessage(mContext, "Please install a maps application")
            }
        }
    }

    fun getDirection2(mContext: Context, mDestinationLatLng: String) {
        val intent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=$mDestinationLatLng")
        )
        intent.setPackage("com.google.android.apps.maps")
        try {
            mContext.startActivity(intent)
        } catch (ex: ActivityNotFoundException) {
            try {
                val unrestrictedIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=$mDestinationLatLng")
                )
                mContext.startActivity(unrestrictedIntent)
            } catch (innerEx: ActivityNotFoundException) {
                showMessage(mContext, "Please install a maps application")
            }
        }
    }

    fun dp2px(context: Context, dp: Float): Float {
        val scale = context.resources.displayMetrics.density
        return dp * scale + 0.5f
    }

    fun sp2px(context: Context, sp: Float): Float {
        val scale = context.resources.displayMetrics.scaledDensity
        return sp * scale
    }

    fun encodeImageToBase64(mContext: Context, filePath: String, name: String): String {
        val currentTimeMillis = System.currentTimeMillis().toString()

        val bytes = File(filePath).readBytes()
        val imageString = Base64.encodeToString(bytes, Base64.NO_WRAP)
        //val file = File(mContext.getCacheDir(), currentTimeMillis+"_"+name)
        //file.writeText(imageString)
        //return file.path
        return imageString
    }

    fun decodeBase64ToImage(mContext: Context, base64: String, name: String): String {
        val bytes = Base64.decode(base64, Base64.NO_WRAP)
        val file = File(mContext.cacheDir, name)
        file.writeBytes(bytes)
        return file.path
    }

    // this set the cursor at the text length of edittext
    fun setEdtCursorAtText(edtView: MyCustomEditText) {
        edtView.text?.let { edtView.setSelection(it.length) }
    }

    // start whatsapp
    fun startWhatsApp(mContext: Context) {
        val contactNumber = ""
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/+$contactNumber/"))
        mContext.startActivity(browserIntent)
    }


    fun getStatus(status: String): String {
        return when (status) {
            "0" -> "Pending"
            "1" -> "Completed"
            "2" -> "Failed"
            "3" -> "Rejected"
            else -> ""
        }
    }

    fun getTransType(type: String): String {
        return when (type) {
            "Cr" -> "+"
            "Dr" -> "-"
            else -> ""
        }
    }

    fun mSendIntent(redirect: String?, context: Context) {
        try {
            if (!TextUtils.isEmpty(redirect) && redirect?.isNotEmpty()!!) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(redirect)
                context.startActivity(i)
            }
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }
    fun copyTextToClipboard(mContext: Context, view1: String) {
        var textToCopy: String? = null

        // Text copy
        textToCopy = view1

        if (textToCopy != null) {
            val clipboardManager =
                mContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", textToCopy)
            clipboardManager.setPrimaryClip(clipData)
            message(mContext,"Details copied")
        }
    }

    fun getExtension(fileName: String): String {
        return fileName.substring(
            if (fileName.lastIndexOf(".") > 0) fileName
                .lastIndexOf(".") + 1 else return "", fileName.length
        )
    }

    fun isEmptyText(view: View?): Boolean {
        return if (view == null)
            true
        else
            getTextValue(view).isEmpty()
    }

    fun getTextValue(view: View): String {
        return (view as? EditText)?.text?.toString()?.trim { it <= ' ' }
            ?: ((view as? TextView)?.text?.toString()?.trim { it <= ' ' }
                ?: "")

    }

    fun showToastMessage(context: Context, mgs: String, isShort: Boolean) {
        Toast.makeText(context, mgs, if (isShort) Toast.LENGTH_SHORT else Toast.LENGTH_SHORT).show()
    }

    fun checkIsVideoFile(path: String): Boolean {
        var extension = ""
        val i = path.lastIndexOf('.')
        if (i > 0) {
            extension = path.substring(i + 1)
        }
        return extension.equals("MP4", ignoreCase = true)

    }

    fun hideKeyBoard(context: Context, view: View) {
        try {
            val inputMethodManager = context.getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun saveMediaToStorage(context: Context, bitmap: Bitmap): Uri? {
        if (bitmap == null)
            return null
        var uri: Uri? = null
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
                uri = imageUri
            }
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)

            uri = Uri.parse(image.absolutePath)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return uri
    }


    fun isMobileNumber(mContext: Context, mm: String): Boolean {
        var mobileNumber = mm
        mobileNumber = mobileNumber.replace(" ", "")
        mobileNumber = mobileNumber.replace("one", "1").replace("1️⃣", "1")
        mobileNumber = mobileNumber.replace("two", "2").replace("1️⃣", "2")
        mobileNumber = mobileNumber.replace("three", "3").replace("1️⃣", "3")
        mobileNumber = mobileNumber.replace("four", "4").replace("1️⃣", "4")
        mobileNumber = mobileNumber.replace("five", "5").replace("1️⃣", "5")
        mobileNumber = mobileNumber.replace("six", "6").replace("1️⃣", "6")
        mobileNumber = mobileNumber.replace("seven", "7").replace("1️⃣", "7")
        mobileNumber = mobileNumber.replace("eight", "8").replace("1️⃣", "8")
        mobileNumber = mobileNumber.replace("nine", "9").replace("1️⃣", "9")

        val result = Regex("[0-9]+").findAll(mobileNumber)
            .map(MatchResult::value)
            .toList()
        val result1 = Regex("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}").findAll(mm)
            .map(MatchResult::value)
            .toList()

        val result2 = Regex(
            "((https?|ftp|smtp)://)?((ht|f)tp(s?)://|www\\.)?(?:^|[\\W])((ht|f)tp(s?)://|www\\.)(([\\w\\-]+\\.){1,}?([\\w\\-.~]+/?)*[\\p{Alnum}.,%_=?&#\\-+()\\[\\]*$~@!:/{};']*)"
//        val result2 = Regex("^((https?|ftp|smtp)://)?((ht|f)tp(s?)://|www\\.)?[a-z0-9]+(\\.[a-z]{2,}){1,3}(#?/?[a-zA-Z0-9#]+)*/?(\\?[a-zA-Z0-9-_]+=[a-zA-Z0-9-%]+&?)?\$"
        ).findAll(mm)
            .map(MatchResult::value)
            .toList()

        val finalResult = ArrayList<String>()

        var isSendMessage = true
        result.map {
            if (it.length > 6) {
                finalResult.add(it)
                isSendMessage = false
            }
        }

        finalResult.addAll(result1)
        //finalResult.addAll(result2)

        if (!result1.isNullOrEmpty()) {
            isSendMessage = false
        }
        /*if (!result2.isNullOrEmpty()) {
        isSendMessage = false
    }*/
        val finalData = finalResult.joinToString(separator = ",")

        if (!isSendMessage) {
            showMessage(mContext, "$finalData Not Allow to send this message")
        }
        LogHelper.e(
            "Mobile Number",
            mobileNumber.toString() + "Mobile Number ${result}" + "Email ${result1}" + "Website ${result2}"
        )
        return isSendMessage
    }

    interface OnItemClickCallback {
        fun OnClickLogin()
    }

    fun inviteToTango(mContext: Context, url: String) {
        val url = url
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        mContext.startActivity(i)
    }

    fun formatCount(count: String): String {
        val counting = count.toInt()
        val formattedCount: String

        when {
            counting >= 1000000000 -> {
                // Format the count in billions
                val billionCount = counting / 1000000000.0
                formattedCount = String.format("%.1fB", billionCount)
            }

            counting >= 1000000 -> {
                // Format the count in millions
                val millionCount = counting / 1000000.0
                formattedCount = String.format("%.1fM", millionCount)
            }

            counting >= 1000 -> {
                // Format the count in thousands
                val thousandCount = counting / 1000.0
                formattedCount = String.format("%.1fK", thousandCount)
            }

            else -> {
                // Format the count as-is
                formattedCount = counting.toString()
            }
        }

        return formattedCount
    }

    /**
     * crop activity
     */
    fun startCropActivity(uri: Uri, activity: Activity, x: Float, y: Float, isHideControls: Boolean) {
        /*var uCrop = UCrop.of(uri, getUri(activity))

        uCrop = uCrop.withAspectRatio(x, y)  // set image crop ratio
        val option = UCrop.Options()
        option.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        option.setCompressionQuality(100)
        if (isHideControls) {
            option.setHideBottomControls(true)
            option.setFreeStyleCropEnabled(false)
        }
        uCrop = uCrop.withOptions(option)
        uCrop.start(activity)*/
    }

    /**
     * Crop activity
     */
    fun startCropActivity(uri: Uri, activity: Activity, isHideControls: Boolean) {
        /*var uCrop = UCrop.of(uri, getUri(activity))

        // Do not set any fixed aspect ratio for free-style cropping
        val option = UCrop.Options()
        option.setCompressionFormat(Bitmap.CompressFormat.JPEG)
        option.setCompressionQuality(100)

        // Enable free-style cropping
        option.setFreeStyleCropEnabled(true)

        if (isHideControls) {
            option.setHideBottomControls(true)
        }

        uCrop = uCrop.withOptions(option)
        uCrop.start(activity)*/
    }



    fun getChatTimeAgo(msgTimeStamp: Long): String {
        //val msgDate = msgTimeStamp.toDate()
        val now = System.currentTimeMillis()
        val diff = now - msgTimeStamp
        val second = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minute = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hour = TimeUnit.MILLISECONDS.toHours(diff)
        val day = TimeUnit.MILLISECONDS.toDays(diff)

        if (second < 1) {
            return "Just now"
        } else if (second < 60) {
            return if (second == 1L) "$second sec" else "$second secs ago"
        } else if (minute < 60) {
            return if (minute == 1L) "$minute min" else "$minute mins ago"
        } else if (hour < 24) {
            return if (hour == 1L) "$hour hour ago" else "$hour hours ago"
        } else if (day >= 7) {
            if (day >= 365) {
                val tempYear = day / 365
                return if (tempYear == 1L) "$tempYear year ago" else "$tempYear years ago"
            } else if (day >= 30) {
                val tempMonth = day / 30
                return if (tempMonth == 1L) (day / 30).toString() + " month ago" else (day / 30).toString() + " months ago"
            } else {
                val tempWeek = day / 7
                return if (tempWeek == 1L) (day / 7).toString() + " week ago" else (day / 7).toString() + " weeks ago"
            }
        } else {
            return if (day == 1L) "$day day ago" else "$day days ago"
        }
    }

    fun Boolean.toInt() = if (this) 1 else 0

    fun getDisplayMetrics(activity: Activity?): DisplayMetrics {
        val display = activity?.windowManager?.defaultDisplay
        val metrics = DisplayMetrics()
        display?.getMetrics(metrics)
        return metrics
    }

    fun setTextSpacing(
        firstValue: String,
        secondValue: String,
        firstTextSize: Int,
        secondTextSize: Int
    ): SpannableString {
        val fullText = firstValue + secondValue
        val spannable = SpannableString(fullText)
        spannable.setSpan(
            AbsoluteSizeSpan(firstTextSize, false),
            0,
            firstValue.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            AbsoluteSizeSpan(secondTextSize, false),
            firstValue.length,
            fullText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannable
    }

    fun String.capitalized(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase())
                it.titlecase(Locale.getDefault())
            else it.toString()
        }
    }

    fun getNavigationBarHeight(view: View): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsets = view.rootWindowInsets
            windowInsets?.getInsets(WindowInsets.Type.navigationBars())?.bottom ?: 0
        } else {
            val decorView = view.rootView as? FrameLayout
            decorView?.paddingBottom ?: 0
        }

    }


    private fun applicantTypFromName(mContext: Context, name: String?) {

    }

    fun makeCall(mContext: Context, number: String) {
        val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
        mContext.startActivity(intent)
    }
}
fun gotoSettings(mContext: Context) {
    try {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", mContext.packageName, null)
        intent.data = uri
        mContext.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun createPartFromString(descriptionString: String): RequestBody {
    return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString)
}

fun String?.isServerImageUrl(): Boolean {
    return this?.startsWith("https://") == true || this?.startsWith("http://") == true
}

