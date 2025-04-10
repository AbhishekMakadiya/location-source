package com.location.location.ui.base

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.InputFilter
import android.text.Spanned
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewbinding.ViewBinding
import com.location.location.constants.Const
import com.location.location.utils.alertmessages.AlertMessageListener
import com.google.android.material.textfield.TextInputLayout
import com.location.location.R

import com.location.location.utils.LogHelper
import com.location.location.utils.PreferenceManager
import com.location.location.utils.Util
import com.location.location.utils.alertmessages.AlertMessage
import java.io.Serializable
import javax.inject.Inject


abstract class BaseActivity<MBinding: ViewBinding> : AppCompatActivity() {
    @Inject
    lateinit var mPreferenceManager: PreferenceManager
    internal var mContext: Context = this
    private var progress: View? = null
    var TAG = this.javaClass.simpleName
    lateinit var mBinding: MBinding
    val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        Manifest.permission.READ_MEDIA_IMAGES

    } else {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    }
    var logoutDialogCount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreferenceManager = PreferenceManager(mContext)
        mBinding = getViewBinding()
    }

    val secureKey: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(mContext.contentResolver, Settings.Secure.ANDROID_ID)

    /**
     *
     * In ViewBinding getting bindViews() override method, need to require override this bellow method
     *
     */
    override fun setContentView(view: View?) {
        super.setContentView(view)
        bindViews()
    }

    /*override fun setContentView(layoutResourceId: Int) {
        super.setContentView(layoutResourceId)
        //bindViews()
    }*/

    fun setUpToolBar(title: CharSequence, upIndicatorResourceId: Int) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
            //val toolbar = mBinding.root.toolbar1
        toolbar.let {
            setSupportActionBar(toolbar)

            supportActionBar.let { mSupportActionBar ->
                mSupportActionBar?.setDisplayHomeAsUpEnabled(true)
                mSupportActionBar?.setDisplayShowTitleEnabled(false)
                if (upIndicatorResourceId != 0)
                    mSupportActionBar?.setHomeAsUpIndicator(upIndicatorResourceId)
            }

            (findViewById<TextView>(R.id.txtTitle)).setText(title)
            //mBinding.root.toolbar_title?.text = title
        }
    }

    fun setTitle(title: String) {
        try {
            (findViewById<TextView>(R.id.txtTitle)).setText(title)
            //mBinding.root.toolbar_title.text = title
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUpToolBar(resourceId: Int) {
        try {
            setUpToolBar(getString(resourceId), 0)
        } catch (e: Exception) {
            setUpToolBar("", 0)
            e.printStackTrace()
        }

    }

    fun setUpToolBar(title: String) {
        setUpToolBar(title, R.drawable.ic_back)
    }

    fun disableBackIcon() {
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            Util.hideKeyboard(mContext)
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    protected abstract fun bindViews()
    abstract fun getViewBinding(): MBinding

    /**
     * set progress view visibility
     * */
    private fun setProgressLayout(visibility: Int) {
        try {
            progress = findViewById(R.id.progressView)
            //progress = mBinding.root.progressView
            progress?.visibility = visibility

//            Util.loadGifImage(mContext, findViewById(R.id.imgGif))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressView() {
        setProgressLayout(View.VISIBLE)
    }

    fun hideProgressView() {
        setProgressLayout(View.GONE)
    }

    /*override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(base!!))
    }*/

    /**
     * check edittext is empty or not
     */
    fun isEmptyEditText(
        editText: EditText
    ): Boolean {
        return TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })
    }

    /**
     * check edittext is empty or not and show error msg if it is empty
     */
    fun isEmptyEditText(
        editText: EditText,
        errorMessage: String,
    ): Boolean {
        return if (TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
            editText.requestFocus()
            editText.error = errorMessage
            //AlertMessage.showMessage(editText.context, errorMessage)
            true
        } else
            false
    }

    /**
     * check edittext is empty or not and show error msg if it is empty
     */
    fun isEmptyEditText(
        mTextInputLayout: TextInputLayout,
        editText: EditText,
        errorMessage: String,
    ): Boolean {
        return if (TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
            editText.requestFocus()
            mTextInputLayout.error = errorMessage
            //AlertMessage.showMessage(editText.context, errorMessage)
            true
        } else
            false
    }
    fun restrictEmoji(editText: EditText) {
        val filters = arrayOf<InputFilter>(object : InputFilter {
            override fun filter(
                source: CharSequence?,
                start: Int,
                end: Int,
                dest: Spanned?,
                dstart: Int,
                dend: Int
            ): CharSequence? {
                for (index in start until end) {
                    val type = Character.getType(source!![index])
                    if (type == Character.SURROGATE.toInt() || type == Character.OTHER_SYMBOL.toInt()) {
                        return "" // Remove emoji characters
                    }
                }
                return null // Accept other characters
            }
        })
        editText.filters = filters
    }


    override fun onResume() {
        super.onResume()
        ContextCompat.registerReceiver(mContext, sessionReceiver, IntentFilter(Const.ACTION_SESSION_EXPIRE), ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(sessionReceiver)
    }

    private var sessionReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Const.ACTION_SESSION_EXPIRE) {
                clearData()
                // build alert dialog
                if(logoutDialogCount < 1)
                {
                    AlertMessage.showMessageWithTitle(mContext,getString(R.string.logout_title),getString(R.string.logout_msg), getString(R.string.login), object : AlertMessageListener(){
                        override fun onPositiveButtonClick() {
                            super.onPositiveButtonClick()
                            // start login activity
                            /*val intent = Intent(mContext, LoginActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finishAffinity()*/
                        }
                    })
                    logoutDialogCount += 1
                }
            }
        }
    }
    fun clearAllNotifications() {
        try {
            val notification = mContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notification.cancelAll()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun clearData(){
        clearAllNotifications()
        mPreferenceManager.clearPreference()
    }
    /**
     * check permission for location
     */
    fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED
    }

    // https://stackoverflow.com/questions/73019160/the-getparcelableextra-method-is-deprecated/73311814#73311814
    fun <T : Serializable?> Intent.getSerializableCompact(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializableCompact(key, clazz)!! else (getSerializableExtra(key) as T?)
    }

    inline fun <reified T : Serializable?> Intent.getSerializableExtraCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSerializableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getSerializableExtra(key) as? T
        }
    }

    fun <T : Serializable?> Bundle.getSerializableCompact(key: String, clazz: Class<T?>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) getSerializable(key, clazz)!! else (getSerializable(key) as T?)
    }
    fun openUrl(redirect: String) {
        try {
            if (!TextUtils.isEmpty(redirect) && redirect.isNotEmpty()) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(redirect)
                mContext.startActivity(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun applyWindowInsetsListener(view: View?) {
        try {
            view?.let {
                ViewCompat.setOnApplyWindowInsetsListener(it) { v, insets ->
                    /*val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                        insets*/
                    // Get the system bars and keyboard (IME) insets
                    val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                    val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())

                    // Check if the keyboard is visible
                    val isKeyboardVisible = insets.isVisible(WindowInsetsCompat.Type.ime())

                    // Adjust padding based on whether the keyboard is visible or not
                    if (isKeyboardVisible) {
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, imeInsets.bottom)
                    } else {
                        v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                    }
                    insets
                }
            }
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    fun setLightStatusBarAppearance(isLight: Boolean = false) {
        // Get the WindowInsetsControllerCompat
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView)

        // Set true light status bar (for dark icons and text) other wie false
        windowInsetsController?.isAppearanceLightStatusBars = isLight
    }
}
