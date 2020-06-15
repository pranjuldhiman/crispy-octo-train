package com.android.roundup

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.otp.OtpActivity
import com.android.roundup.utils.Constants.GLOBAL_TAG
import com.android.roundup.utils.Constants.PHONE_NUMBER
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
        configureUi()
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
}
