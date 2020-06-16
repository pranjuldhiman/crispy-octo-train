package com.android.roundup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.otp.OtpActivity
import com.android.roundup.utils.Constants.GLOBAL_TAG
import com.android.roundup.utils.Constants.PHONE_NUMBER
import com.android.roundup.utils.Util
import com.android.roundup.utils.Util.IS_LOGGED_IN
import com.android.roundup.utils.Util.getLoginData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        setContentView(R.layout.login_activity)
        if (getLoginData(this, IS_LOGGED_IN)){
            launchMainActivity()
        }else{
            configureUi()
        }

    }

    init {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(GLOBAL_TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result?.token
                Log.d(GLOBAL_TAG, token)
            })
    }

    private fun configureUi() {
        btn_verify.setOnClickListener {
            startActivity(
                Intent(this@LoginActivity, OtpActivity::class.java).putExtra(
                    PHONE_NUMBER, "+91".plus(et_phone.text.toString())
                ))
        }
    }

    private fun launchMainActivity() {
        startActivity(
            Intent(this@LoginActivity, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
