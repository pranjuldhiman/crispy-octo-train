package com.android.roundup.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.LoginActivity
import com.android.roundup.MainActivity
import com.android.roundup.R

private const val TIME_OUT_TIME: Long = 3000
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_splash)
        configureHandler()
    }

    private fun configureHandler() {
        /*Handler().postDelayed(Runnable {
            if (LimeSharedRepositoryImpl(this).isLoggedIn)
                startActivity(Intent(this, MainActivity::class.java))
            else
                startActivity(Intent(this, LoginActivity::class.java))
            this.finish()
        }, TIME_OUT_TIME)*/
    }
}
