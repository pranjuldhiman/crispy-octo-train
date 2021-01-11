package com.rs.roundupclasses.login

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.MainActivity
import com.rs.roundupclasses.otp.OtpActivity
import com.rs.roundupclasses.utils.ApplicationPrefs
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.Constants.GLOBAL_TAG
import com.rs.roundupclasses.utils.Constants.PHONE_NUMBER
import com.rs.roundupclasses.utils.ProgressDialog
import com.rs.roundupclasses.utils.RoundUpHelper
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity : AppCompatActivity(){

    lateinit  var progress: ProgressDialog;
    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { LoginViewModel(this) }).get(
            LoginViewModel::class.java)
    }

    lateinit var sharedPreferences: SharedPreferences
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        sharedPreferences = this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)

        setContentView(R.layout.login_activity)
        progress = ProgressDialog(this)

        if (ApplicationPrefs.isLoggedIn()){
            launchMainActivity()
        }else{
            configureUi()
        }

      /*  btn_verify.setOnClickListener {

            viewModel.getLogin(mobileno.text.toString())
        }*/
        setViewModelObservers()

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
            if(mobileno.text.toString().equals(""))
            {
                Toast.makeText(this,"Mobile no is required",Toast.LENGTH_SHORT).show()
            }
            else
            {
                viewModel.getLogin(mobileno.text.toString())

            }
        }
    }

    private fun launchMainActivity() {
        startActivity(
            Intent(this@LoginActivity, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun setViewModelObservers() {
        viewModel.apply {


            listOfSearchResult.observe(this@LoginActivity, Observer {

                sharedPreferences.edit { putString(Constants.USERNAME,name.value.toString()) }
                sharedPreferences.edit { putString(Constants.USEREMAIL,email.value.toString()) }
                sharedPreferences.edit { putString(Constants.USERMOB,mobileno.text.toString()) }
                sharedPreferences.edit { putString(Constants.USERID,listOfSearchResult.value.toString()) }

                startActivity(
                    Intent(this@LoginActivity, OtpActivity::class.java).putExtra(
                        PHONE_NUMBER, "+91".plus(mobileno.text.toString())
                    ))
            })
            serviceException.observe(this@LoginActivity, Observer {
                showMessageDialog(it.orEmpty())
            })
        }
    }

    private fun showMessageDialog(msgWrapper: String?) {
        msgWrapper?.let {
            val msg = it
            AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    //startActivity(Intent(this, ScanActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                .create().run {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }
}
