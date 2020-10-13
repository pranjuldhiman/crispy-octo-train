package com.android.roundup.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.R
import com.android.roundup.WelcomeActivity
import com.android.roundup.dashboard.MainActivity
import com.android.roundup.library.LibraryActivity
import com.android.roundup.login.LoginActivity
import com.android.roundup.utils.ApplicationPrefs

private const val TIME_OUT_TIME: Long = 3000
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_splash)
        configureHandler()
    }

    private fun configureHandler() {
        Handler().postDelayed(Runnable {
        //  startActivity(Intent(this, MainActivity::class.java))
           if (ApplicationPrefs.isLoggedIn())
             startActivity(Intent(this, MainActivity::class.java))
            else{
                if (ApplicationPrefs.isNotFirstTime()){
                    startActivity(Intent(this, LoginActivity::class.java))
                }else{
                    startActivity(Intent(this, WelcomeActivity::class.java))
                }
            }
            this.finish()
        }, TIME_OUT_TIME)
    }
}
