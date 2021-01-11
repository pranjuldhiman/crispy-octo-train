package com.rs.roundupclasses.profile

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.rs.roundupclasses.R
import com.rs.roundupclasses.utils.*
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.profile_activity.*
import kotlinx.android.synthetic.main.profile_activity.back_icons

class ProfileActivity : AppCompatActivity()
{
    lateinit  var progress: ProgressDialog;
    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { ProfileViewModel(this) }).get(
            ProfileViewModel::class.java)
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
        setContentView(R.layout.profile_activity)
        progress = ProgressDialog(this)
        sharedPreferences = this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()
        var mobno: String= sharedPreferences.getString(Constants.USERMOB,"").toString()
        var emailvalue: String= sharedPreferences.getString(Constants.USEREMAIL,"").toString()
        var namevale: String= sharedPreferences.getString(Constants.USERNAME,"").toString()

        name.setText(namevale.toString())
        email.setText(emailvalue.toString())
        mobilenotxt.setText(mobno.toString())

        val updatebtn =
            findViewById<View>(R.id.update) as RelativeLayout

        updatebtn.setOnClickListener {

            if(name.text.toString().equals(""))
            {
                Toast.makeText(this,"Name is required",Toast.LENGTH_LONG).show()
            }
            else if(email.text.toString().equals(""))
            {
                Toast.makeText(this,"Email is required",Toast.LENGTH_LONG).show()
            }
            else if(!ValidationUtil.isEmailValid(email.text.toString()))
                {
                    Toast.makeText(this,"Enter valid email",Toast.LENGTH_LONG).show()
                }
            else
            {
                viewModel.editProfile(name.text.toString(),email.text.toString(),mobno.toString(),userid.toString())
            }
        }

        back_icons.setOnClickListener {
            onBackPressed()
        }

        viewModel.status.observe(this, Observer {
            when (it) {
                ApiStatus.LOADING -> {
                    progress.setLoading(true)
                }
                ApiStatus.ERROR -> {
                    progress.setLoading(false)
                }
                ApiStatus.DONE -> {
                    progress.setLoading(false)
                }
            }
        })
        setViewModelObservers()
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            message.observe(this@ProfileActivity, Observer {
                sharedPreferences.edit { putString(Constants.USERNAME,name.text.toString()) }
                sharedPreferences.edit { putString(Constants.USEREMAIL,email.text.toString()) }
                Toast.makeText(this@ProfileActivity,message.value.toString(),Toast.LENGTH_LONG).show()
            })
            serviceException.observe(this@ProfileActivity, Observer {
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