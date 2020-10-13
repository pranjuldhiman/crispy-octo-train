package com.android.roundup.library

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.roundup.R
import com.android.roundup.chooseicon.ChooseIconActivity
import com.android.roundup.dashboard.MainViewModel
import com.android.roundup.dashboard.adapter.*
import com.android.roundup.dashboard.adapter.VideoItemDecoration
import com.android.roundup.dashboard.model.Subcategory
import com.android.roundup.examcontent.ExamContentActivity
import com.android.roundup.scan.ScanActivity
import com.android.roundup.subjectlist.SubjectActivity
import com.android.roundup.utils.ApiStatus
import com.android.roundup.utils.Constants
import com.android.roundup.utils.ProgressDialog
import com.android.roundup.utils.RoundUpHelper
import com.android.roundup.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.subject_activity.*

class LibraryActivity : AppCompatActivity() {
    lateinit  var progress: ProgressDialog;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var type: String
    lateinit var catid: String

    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { MainViewModel(this) }).get(
            MainViewModel::class.java)
    }

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
        sharedPreferences = this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)

        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()

        Log.e("SHAREDPREFERENCE","userid is....."+userid)

        setContentView(R.layout.library_activity)
        progress = ProgressDialog(this)
        //   loadViewPager()
        getDashboardData(userid)
        setViewModelObservers()

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








    private fun getDashboardData(userid:String) {
        viewModel.getDashboardData(userid)
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(this@LibraryActivity, Observer {
                var value:Boolean = true

                for(data in it!!.indices)
                {

                    if(it.get(data).subcategory!!.size>0)
                    {

                        for(datas in it.get(data).subcategory!!.indices)
                        {
                            catid =   it.get(data).subcategory!!.get(datas).id.toString()
                            type =   it.get(data).subcategory!!.get(datas).name.toString()
                            break
                        }
                        Log.e("CATEGORYNAME","categoryname name name name is......"+type)
                        Log.e("CATEGORYNAME","categoryname id id id id is......"+catid)

                        Log.e("CATEGORYNAME","categoryname is......"+it.get(data).category)
                        break
                    }
                    else
                    {
                        Log.e("CATEGORYNAME","categoryname else else is......"+it.get(data).category)
                    }
                }



                rcv_dashboard.visibility = View.VISIBLE
                rcv_dashboard.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = DashBoardCategoryAdapter({
                        if(it.having_subcategory.equals("1"))
                        {
                            var list:ArrayList<Subcategory> = it.subcategory as ArrayList<Subcategory>
                            //   context.startActivity(Intent(context, ChooseIconActivity::class.java))
                            val intent = Intent(context, ChooseIconActivity::class.java)
                            intent.putExtra("type", it.category)
                            intent.putExtra("LIST", list)
                            startActivity(intent)
                        }
                        else
                        {
                            context.startActivity(Intent(context, ExamContentActivity::class.java))
                        }
                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(0))
                }
            })




            serviceException.observe(this@LibraryActivity, Observer {
                rcv_dashboard.visibility = View.GONE
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



    private fun loadViewPager() {
        val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        //   val viewPagerAdapter = AdsAdapter(this)
        //  viewPager.adapter = viewPagerAdapter
    }
}