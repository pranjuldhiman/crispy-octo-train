package com.rs.roundupclasses.subjectlist

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.aftersubbjectlist.AfterSubjectList
import com.rs.roundupclasses.dashboard.adapter.VideoItemDecoration
import com.rs.roundupclasses.scan.ScanActivity
import com.rs.roundupclasses.utils.ApiStatus
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.ProgressDialog
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.subject_activity.*

class SubjectActivity : AppCompatActivity() {

    //    //class name
    lateinit  var progress: ProgressDialog;
    lateinit var sharedPreferences: SharedPreferences

    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { SubjectViewModel(this) }).get(
            SubjectViewModel::class.java)
    }

    lateinit  var id:String;
    override fun onCreate(savedInstanceState: Bundle?) {
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            ScanActivity.setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        super.onCreate(savedInstanceState)
        progress = ProgressDialog(this)

        setContentView(R.layout.subject_activity)
        id = intent.getStringExtra("ID")
        var type = intent.getStringExtra("type")

        Log.e("CATEGORYNAME","final final id is......"+id.toString())
        Log.e("CATEGORYNAME","final type is......"+type.toString())

        sharedPreferences = this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()


        viewModel.getSubjectData(userid,id)
        setViewModelObservers()

        toolbartex.text = type.toString()
       /* subject_list.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SubjectAdapter({
            },context, emptyList())
            addItemDecoration(GridDividerItemDecoration(3))
        }*/
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
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(this@SubjectActivity, Observer {
                subject_list.visibility = View.VISIBLE
                subject_list.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = SubjectAdapter({ s: String, nme: String ->

                        val intent = Intent(context, AfterSubjectList::class.java)
                        intent.putExtra("type", nme)
                        intent.putExtra("ID", s)
                        context.startActivity(intent)


                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(10))
                }
            })

            serviceException.observe(this@SubjectActivity, Observer {
                subject_list.visibility = View.GONE
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
    // view all 1
    // 0 pdf videos
}