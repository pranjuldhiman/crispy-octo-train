package com.android.roundup.notify

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.notify.NotificationAdapter
import com.android.notify.NotificationViewModel
import com.android.roundup.R
import com.android.roundup.dashboard.adapter.VideoItemDecoration
import com.android.roundup.utils.ApiStatus
import com.android.roundup.utils.Constants
import com.android.roundup.utils.ProgressDialog
import com.android.roundup.utils.RoundUpHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.notification_activity.*
import kotlinx.android.synthetic.main.subject_activity.*
import kotlinx.android.synthetic.main.subject_activity.back_icons

class NotificationActivity : AppCompatActivity()
{
    lateinit  var progress: ProgressDialog;
    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { NotificationViewModel(this) }).get(
            NotificationViewModel::class.java)
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

        sharedPreferences =
            this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)

        setContentView(R.layout.notification_activity)
        progress = ProgressDialog(this)
        back_icons.setOnClickListener {
            onBackPressed()
        }
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()

        viewModel.getNotificationData(userid)
        setViewModelObservers()

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
    }


     fun setViewModelObservers() {
        viewModel.apply {
            listOfNotificationResult.observe(this@NotificationActivity, Observer {
                notification_list.visibility = View.VISIBLE
                notification_list.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = NotificationAdapter({ s: String->

                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(10))
                }
            })

            serviceException.observe(this@NotificationActivity, Observer {
                notification_list.visibility = View.GONE
                showMessageDialog(it.orEmpty())
            })
        }
    }

     fun showMessageDialog(msgWrapper: String?) {
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