package com.rs.roundupclasses.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rs.roundupclasses.R
import com.rs.roundupclasses.WelcomeActivity
import com.rs.roundupclasses.dashboard.MainActivity
import com.rs.roundupclasses.login.LoginActivity
import com.rs.roundupclasses.utils.ApplicationPrefs
import com.rs.roundupclasses.webview.Feedback

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
