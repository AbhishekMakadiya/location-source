package com.location.location.ui.contentPage

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.location.location.R
import com.location.location.constants.Const
import com.location.location.databinding.ActivityContentPageBinding
import com.location.location.ui.base.BaseActivity
import com.location.location.utils.LogHelper.e
import com.location.location.utils.LogHelper.printStackTrace
import java.io.UnsupportedEncodingException
import java.net.URLEncoder

class ContentPageActivity : BaseActivity<ActivityContentPageBinding>() {

    private var mTitleString: String = ""
    private var mUrl: String = ""
    private var isPdfFile = false
    private var isOrientation = false

    override fun getViewBinding(): ActivityContentPageBinding = ActivityContentPageBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mBinding = ActivityContentPageBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setLightStatusBarAppearance(true)
        ViewCompat.setOnApplyWindowInsetsListener(mBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bindViews()
    }

    override fun bindViews() {
        mUrl = intent.getStringExtra(Const.EXTRA_URI) as String
        mTitleString = intent.getStringExtra(Intent.EXTRA_TITLE) as String
        isPdfFile = intent.getBooleanExtra(Intent.EXTRA_TEXT, false)
        isOrientation = intent.getBooleanExtra(Intent.EXTRA_REFERRER, false)
        setUpToolBar(mTitleString.toString(), R.drawable.ic_back)

        if (isOrientation){
            // Set the orientation to landscape initially
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            mBinding.include.root.visibility = View.GONE

        }else{
            // Allow both orientations after the activity is created
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            mBinding.include.root.visibility = View.VISIBLE

        }


        mBinding.progressBar.visibility= View.VISIBLE
        mBinding.mContentView.webViewClient = WebViewClient()
        mBinding.mContentView.settings.javaScriptEnabled = true

        if (isPdfFile) {

            val fileName = mUrl.substring(mUrl.lastIndexOf("/") + 1, mUrl.length)
            val urlPath = mUrl.substring(0, mUrl.lastIndexOf("/") + 1)
            try { //                fileName=fileName.replace("+"," ");
                var encodedStr = URLEncoder.encode(fileName, "UTF-8")
                encodedStr = encodedStr.replace("+", "%20")
                e(TAG, "encodedStr=$encodedStr")
                encodedStr = URLEncoder.encode(encodedStr, "UTF-8")
                e(TAG, "AgainencodedStr=$encodedStr")
                mUrl = urlPath + encodedStr
            } catch (e: UnsupportedEncodingException) {
                printStackTrace(e)
            }
            //            mContentView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
            //stackoverflow.com/questions/14578530/how-to-open-display-documents-pdf-doc-without-external-app
            mBinding.mContentView.loadUrl("http://docs.google.com/gview?embedded=true&url=$mUrl")
            //set the WebViewClient
            //https://stackoverflow.com/questions/27717214/android-webview-remove-pop-out-option-in-google-drive-doc-viewer
            mBinding.mContentView.webViewClient = object : WebViewClient() {
                //once the page is loaded get the html element by class or id and through javascript hide it.
                override fun onPageFinished(view: WebView, url: String) {
                    super.onPageFinished(view, url)
                    mBinding.mContentView.loadUrl(
                        "javascript:(function() { " +
                                "document.getElementsByClassName('ndfHFb-c4YZDc-GSQQnc-LgbsSe ndfHFb-c4YZDc-to915-LgbsSe VIpgJd-TzA9Ye-eEGnhe ndfHFb-c4YZDc-LgbsSe')[0].style.display='none'; })()"
                    )
                }
            }
        } else {
            mBinding.mContentView.settings.builtInZoomControls = true
            mBinding.mContentView.settings.displayZoomControls = false
            //new to set initial scale to fit screen
            mBinding.mContentView.settings.loadWithOverviewMode = true
            mBinding.mContentView.settings.useWideViewPort = true
            mBinding.mContentView.loadUrl(mUrl)
            e("tag", "url==$mUrl")

        }
        mBinding.mContentView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, progress: Int) {
                mBinding.progressBar.progress = progress
                if (progress == 100) {
                    mBinding.progressBar.visibility = View.GONE
                    mBinding.mContentView.visibility = View.VISIBLE
                }
            }
        }
    }

    companion object {

        fun startActivity(mContext: Context, mUrl: String?, mTitle: String?, isPdfFile: Boolean, isOrientation: Boolean) {
            val intent = Intent(mContext, ContentPageActivity::class.java)
            intent.putExtra(Const.EXTRA_URI, mUrl)
            intent.putExtra(Intent.EXTRA_TITLE, mTitle)
            intent.putExtra(Intent.EXTRA_TEXT, isPdfFile)
            if (isOrientation) {
                intent.putExtra(Intent.EXTRA_REFERRER, isOrientation)
            }
            mContext.startActivity(intent)
        }
    }
}