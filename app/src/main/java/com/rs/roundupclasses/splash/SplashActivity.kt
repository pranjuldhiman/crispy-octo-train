package com.rs.roundupclasses.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.rs.roundupclasses.R
import com.rs.roundupclasses.WelcomeActivity
import com.rs.roundupclasses.dashboard.MainActivity
import com.rs.roundupclasses.login.LoginActivity
import com.rs.roundupclasses.login.LoginViewModel
import com.rs.roundupclasses.utils.ApplicationPrefs
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.RoundUpHelper
import com.rs.roundupclasses.webview.Feedback
import kotlinx.android.synthetic.main.login_activity.*

private const val TIME_OUT_TIME: Long = 5000
class SplashActivity : AppCompatActivity() {

    var fcm_token : String=""

    lateinit var sharedPreferences: SharedPreferences
    var userid:String="";
    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { SplashViewModel(this) }).get(
            SplashViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_splash)

        sharedPreferences = this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
         userid = sharedPreferences.getString(Constants.USERID,"").toString()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("NOTIFICATION", "getInstanceId failed", task.exception)
                    configureUi()
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                //  val msg = getString(0, token)
                Log.e("NOTIFICATION", "token is is....."+token)
                Log.e("NOTIFICATION", "userid is is....."+userid)
                fcm_token = token!!
                // Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                configureUi()
            })
        FirebaseMessaging.getInstance().subscribeToTopic("all");

      //  configureHandler()


    }

    private fun configureUi() {

        if(fcm_token!=null)
        {
            if(userid.equals(""))
            {
                configureHandler()
            }
            else
            {
                viewModel.getLogin(userid.toString(),fcm_token.toString())

                configureHandler()

            }
        }
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
