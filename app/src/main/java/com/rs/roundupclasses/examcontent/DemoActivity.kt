package com.rs.roundupclasses.examcontent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rs.notify.NotificationAdapter
import com.rs.notify.NotificationViewModel
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.adapter.VideoItemDecoration
import com.rs.roundupclasses.examcontent.fragments.DemoAdapter
import com.rs.roundupclasses.examcontent.fragments.PdfFragmentViewModel
import com.rs.roundupclasses.notify.NotificationDetail
import com.rs.roundupclasses.utils.ApiStatus
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.ProgressDialog
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.notification_activity.*
import kotlinx.android.synthetic.main.subject_activity.*

class DemoActivity : AppCompatActivity()
{


    lateinit  var progress: ProgressDialog;
       private val viewModel by lazy {
         ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { PdfFragmentViewModel(this) }).get(
               PdfFragmentViewModel::class.java)
       }

/*    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { NotificationViewModel(this) }).get(
            NotificationViewModel::class.java)
    }*/

    lateinit var sharedPreferences: SharedPreferences
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("DEMOACTIVITY","demo activity is called");

        if (Build.VERSION.SDK_INT >= 21) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        sharedPreferences =
            this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        var topic_id = sharedPreferences.getString(Constants.TOPICID,"")
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()
        setContentView(R.layout.notification_activity)
        progress = ProgressDialog(this)

      viewModel.getVideoData(userid,topic_id!!)
   //     viewModel.getNotificationData(userid)

        setViewModelObserver()

      /*  viewModel.status.observe(this, Observer {
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
        })*/
    }


    fun setViewModelObserver() {
        viewModel.apply {
            listOfSearchResult.observe(this@DemoActivity, Observer {
                notification_list.visibility = View.VISIBLE
                notification_list.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = DemoAdapter({ title: String, message: String, time: String->
                        val intent = Intent(this@DemoActivity, NotificationDetail::class.java)
                        intent.putExtra("TITLE", title)
                        intent.putExtra("MESSAGE", message)
                        intent.putExtra("TIME", time)
                        startActivity(intent)

                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(10))
                }
            })

            serviceException.observe(this@DemoActivity, Observer {
                notification_list.visibility = View.GONE
                showMessageDialog(it.orEmpty())
            })
        }
    }

    /*fun setViewModelObservers() {
        viewModel.apply {
            listOfNotificationResult.observe(this@DemoActivity, Observer {
                notification_list.visibility = View.VISIBLE
                notification_list.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = DemoAdapter({ title: String, message: String, time: String->
                        val intent = Intent(this@DemoActivity, NotificationDetail::class.java)
                        intent.putExtra("TITLE", title)
                        intent.putExtra("MESSAGE", message)
                        intent.putExtra("TIME", time)
                        startActivity(intent)

                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(10))
                }
            })

            serviceException.observe(this@DemoActivity, Observer {
                notification_list.visibility = View.GONE
                showMessageDialog(it.orEmpty())
            })
        }
    }*/

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