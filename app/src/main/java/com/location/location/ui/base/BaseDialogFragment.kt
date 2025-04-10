package com.location.location.ui.base

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialogFragment
import com.location.location.R
import com.location.location.utils.LogHelper
import com.location.location.utils.PreferenceManager

abstract class BaseDialogFragment : AppCompatDialogFragment() {
    lateinit var mContext: Context
    lateinit var mPreferenceManager: PreferenceManager
    val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPreferenceManager = PreferenceManager(mContext)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    abstract fun bindViews()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
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

    /*  fun showProgressView() {
          if (activity is BaseActivity)
              (activity as BaseActivity).showProgressView()
      }

      fun hideProgressView() {
          if (activity is BaseActivity)
              (activity as BaseActivity).hideProgressView()
      }*/
}
