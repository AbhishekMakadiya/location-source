package com.location.location.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.location.location.databinding.ActivitySplashBinding
import com.location.location.ui.base.BaseActivity
import com.location.location.ui.location.activity.PlaceListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(){

    override fun getViewBinding() = ActivitySplashBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }
    override fun bindViews() {
        Handler(Looper.getMainLooper()).postDelayed({

            PlaceListActivity.startActivity(mContext)
            finish()
        }, 1200)
    }


    companion object {
        fun startActivity(mContext: Context) {
            val intent = Intent(mContext, SplashActivity::class.java)
            mContext.startActivity(intent)
        }
    }

}
