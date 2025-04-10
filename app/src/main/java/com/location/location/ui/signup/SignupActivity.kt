package com.location.location.ui.signup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import com.lovebuzz.countrypicker.Country
import com.lovebuzz.countrypicker.CountryPicker
import com.lovebuzz.countrypicker.listeners.OnCountryPickerListener
import com.location.location.R
import com.location.location.constants.Const
import com.location.location.data.remote.NetworkUtils
import com.location.location.data.remote.Status
import com.location.location.data.viewmodels.UserViewModel
import com.location.location.databinding.ActivitySignupBinding
import com.location.location.ui.base.BaseActivity
import com.location.location.utils.LogHelper
import com.location.location.utils.Util
import com.location.location.utils.gone
import com.location.location.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(), View.OnClickListener,
    OnCountryPickerListener {

    private var lastClickTime: Long = 0
    private val mUserViewModel: UserViewModel by viewModels()

    override fun getViewBinding(): ActivitySignupBinding = ActivitySignupBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(mBinding.root)
        setLightStatusBarAppearance(true)
        applyWindowInsetsListener(mBinding.root)
    }

    override fun bindViews() {
        setUpToolBar("", R.drawable.ic_back)

        mBinding.btnCreateAccount.setOnClickListener(this)
        mBinding.edtCountryCode.setOnClickListener(this)
        //mBinding.lblForgotPwd.setOnClickListener(this)
        observeUserRegisterData()
        customTextSignup()
    }

    override fun onClick(v: View?) {
        when (v) {
            mBinding.btnCreateAccount -> {
                // preventing double, using threshold of 1000 ms
                if (SystemClock.elapsedRealtime() - lastClickTime < 1000) {
                    return
                }
                if (isValid()) {
                    if (Util.isConnectedToInternet(mContext)) {
                        callAPIUserSingIn()
                    } else {
                        Util.showMessage(mContext, getString(R.string.error_no_internet))
                    }
                }
                lastClickTime = SystemClock.elapsedRealtime()
            }

            mBinding.edtCountryCode ->{
                showCountryCodePicker()
            }
        }
    }


    private fun showCountryCodePicker() {
        val builder = CountryPicker.Builder().with(this).listener(this)
        builder.theme(CountryPicker.THEME_NEW)
        builder.canSearch(false)
        builder.sortBy(CountryPicker.SORT_BY_NAME)
        val countryPicker = builder.build()
        countryPicker.showBottomSheet(this)
    }

    override fun onSelectCountry(country: Country?) {
        LogHelper.d("testCode", country?.name.toString() + ":" + country?.dialCode)
        mBinding.edtCountryCode.setText(country?.name.toString())
    }




    /**
     *
     * Validation
     *
     **/
    private fun isValid(): Boolean {
        return when {
            isEmptyEditText(mBinding.edtUsername) -> {
                mBinding.edtUsername.requestFocus()
                mBinding.edtUsername.error = getString(R.string.error_name)
                false
            }
            isEmptyEditText(mBinding.edtLastname) -> {
                mBinding.edtLastname.requestFocus()
                mBinding.edtLastname.error = getString(R.string.error_name)
                false
            }
            isEmptyEditText(mBinding.edtPassword) -> {
                mBinding.edtPassword.requestFocus()
                mBinding.edtPassword.error = getString(R.string.error_password)
                false
            }

            isEmptyEditText(mBinding.edtEmail) -> {
                mBinding.edtLastname.requestFocus()
                mBinding.edtLastname.error = getString(R.string.error_name)
                false
            }

            isEmptyEditText(mBinding.edtInstitute) -> {
                mBinding.edtInstitute.requestFocus()
                mBinding.edtInstitute.error = getString(R.string.error_instituter)
                false
            }
            Util.isValidEmailId(mBinding.edtEmail.text.toString()).not() -> {
                    mBinding.edtEmail.requestFocus()
                    mBinding.edtEmail.error = getString(R.string.error_invalid_email)
                false
            }

            mBinding.edtMobileNumber.text.toString().trim().length < 10 -> {
                    mBinding.edtMobileNumber.requestFocus()
                    mBinding.edtMobileNumber.error = getString(R.string.error_invalid_mobile_number)
                return false
            }

            else -> true
        }
    }


    /**
     *
     * Api call for User Login
     *
     **/
    private fun callAPIUserSingIn() {
        val paramMap = HashMap<String, Any?>()
        paramMap[Const.PARAM_FIRST_NAME]=mBinding.edtUsername.text.toString().trim()
        paramMap[Const.PARAM_LAST_NAME]=mBinding.edtLastname.text.toString().trim()
        paramMap[Const.PARAM_EMAIL]=mBinding.edtEmail.text.toString().trim()
        paramMap[Const.PARAM_PASSWORD]=mBinding.edtPassword.text.toString().trim()
        paramMap[Const.PARAM_MOBILE]=mBinding.edtMobileNumber.text.toString().trim()
        paramMap[Const.PARAM_COUNTRY]=mBinding.edtCountryCode.text.toString().trim()
        paramMap[Const.PARAM_INSTITIUTE]=mBinding.edtInstitute.text.toString().trim()
        mUserViewModel.apiUserRegister(paramMap)
    }

    /**
     *
     * Observer for User Login
     *
     **/
    private fun observeUserRegisterData() {
        lifecycleScope.launch {
            mUserViewModel.userRegister.collect {
                when (it.status) {
                    Status.LOADING -> {
                        mBinding.lyProgress.progressView.visible()
                    }

                    Status.SUCCESS -> {
                        mBinding.lyProgress.progressView.gone()
                        val resData = it.data
                        if (resData?.isSuccess == true) {
//                            VerificationActivity.startActivity(mContext, mBinding.edtEmail.text.toString().trim(), false)
                            Toast.makeText(mContext, "OTP -> ${resData.result}", Toast.LENGTH_LONG).show()
                        }  else{
                            Util.showMessage(mContext,it.data?.message)
                        }
                    }

                    Status.ERROR -> {
                        mBinding.lyProgress.progressView.gone()
                        it.throwable?.let {
                            Util.showMessage(mContext, NetworkUtils.getErrorMessage(mContext,it))
                        }
                    }
                    else -> {
                        Util.showMessage(mContext,it.data?.message)
                    }
                }
            }
        }
    }

    private fun openChangePasswordActivityForResult() {
        /*val mIntent = Intent(this, ChangePasswordActivity::class.java)
        mIntent.putExtra(Const.EXTRA_IS_FROM, "1")
        changePasswordResultLauncher.launch(mIntent)*/
    }

    private var changePasswordResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            //val data: Intent? = result.data
        }
    }

    private fun customTextSignup() {
        val spanTxt = SpannableStringBuilder("")
        spanTxt.append(getString(R.string.do_you_have_account))
        spanTxt.append(" ")
        spanTxt.append(getString(R.string.signin))
        spanTxt.setSpan(
            object : ClickableSpan() {
                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = ContextCompat.getColor(mContext, R.color.textColorDarkBlue)
                    ds.isUnderlineText = false
                    ds.typeface = ResourcesCompat.getFont(mContext, R.font.roboto_medium)
                    ds.bgColor = ContextCompat.getColor(mContext, android.R.color.transparent)
                }

                override fun onClick(widget: View) {
                    onBackPressedDispatcher.onBackPressed()
                }
            },
            spanTxt.length - getString(R.string.signin).length,
            spanTxt.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mBinding.txtSignup.highlightColor = ContextCompat.getColor(mContext, android.R.color.transparent)
        mBinding.txtSignup.movementMethod = LinkMovementMethod.getInstance()
        mBinding.txtSignup.setText(spanTxt, TextView.BufferType.SPANNABLE)
    }

    companion object {
        fun startActivity(mContext: Context) {
            val intent = Intent(mContext, SignupActivity::class.java)
            mContext.startActivity(intent)
        }
    }
}