package com.location.location.ui.base

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.location.location.R
import com.location.location.utils.LogHelper
import com.location.location.utils.PreferenceManager

abstract class BaseFragment<MBinding: ViewBinding> : Fragment() {
    lateinit var mContext: Context
    lateinit var mPreferenceManager: PreferenceManager
    val TAG = this.javaClass.simpleName

    lateinit var mBinding: MBinding
    val PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

        Manifest.permission.READ_MEDIA_IMAGES

    } else {
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreferenceManager = PreferenceManager(mContext)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mBinding = getViewBindingFragment()
        mContext = context
    }

    abstract fun bindViews(view: View)
    abstract fun getViewBindingFragment(): MBinding               // reference : https://dev.to/enyason/how-to-set-up-a-base-fragment-class-with-viewbinding-and-viewmodel-on-android-57g1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //mBinding = getViewBindingFragment()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //mView = view
        bindViews(view)
    }

    // set up toolbar
    protected fun setUpToolBar(msg: String) {
        try {
            val txtToolbarTitle = requireActivity().findViewById<TextView>(R.id.txtTitle)
            txtToolbarTitle.text = msg
        } catch (e: Exception) {
            LogHelper.printStackTrace(e)
        }
    }

    /**
     * set progress view visibility
     * */
    private fun setProgressLayout(mView: View, visibility: Int) {
        try {
            mView.visibility = visibility
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showProgressView(mView: View) {
        return setProgressLayout(mView, View.VISIBLE)
    }

    fun hideProgressView(mView: View) {
        return setProgressLayout(mView, View.GONE)
    }
    fun openUrl(redirect: String) {
        try {
            if (!TextUtils.isEmpty(redirect) && redirect.isNotEmpty()) {
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(redirect)
                requireActivity().startActivity(i)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
    fun isEmptyEditText(
        editText: EditText
    ): Boolean {
        return if (TextUtils.isEmpty(editText.text.toString().trim { it <= ' ' })) {
            true
        } else
            false
    }
}
