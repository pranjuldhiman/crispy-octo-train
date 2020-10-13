package com.android.roundup.dashboard

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
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.android.roundup.R
import com.android.roundup.chooseicon.ChooseIconActivity
import com.android.roundup.dashboard.adapter.*
import com.android.roundup.dashboard.model.Subcategory
import com.android.roundup.examcontent.ExamContentActivity
import com.android.roundup.library.LibraryActivity
import com.android.roundup.notify.NotificationActivity
import com.android.roundup.profile.ProfileActivity
import com.android.roundup.scan.ScanActivity
import com.android.roundup.subjectlist.SubjectActivity
import com.android.roundup.utils.ApiStatus
import com.android.roundup.utils.Constants
import com.android.roundup.utils.ProgressDialog
import com.android.roundup.utils.RoundUpHelper
import com.android.roundup.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit  var progress:ProgressDialog;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var type: String
    lateinit var catid: String

    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { MainViewModel(this) }).get(MainViewModel::class.java)
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

        setContentView(R.layout.activity_main)
        progress = ProgressDialog(this)
        configureUi()
     //   loadViewPager()
        getDashboardData(userid)
        setViewModelObservers()
        distance(30.7387,76.7807,30.7088,76.8384)

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


    private fun distance(
        lat1: Double,
        lon1: Double,
        lat2: Double,
        lon2: Double
    ): Double {
        val theta = lon1 - lon2
        var dist = (Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta))))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        Log.e("LATLONG","distance is......"+dist)

        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

    fun getLocation()
    {
        val loc1 = Location("")
        loc1.setLatitude(30.7387)
        loc1.setLongitude(76.7807)

        val loc2 = Location("")
        loc2.setLatitude(30.7088)
        loc2.setLongitude(76.8384)

        val distanceInMeters: Float = loc1.distanceTo(loc2)

        Log.e("LATLONG","distance is......"+distanceInMeters)
    }

    private fun getDashboardData(userid:String) {
        viewModel.getDashboardData(userid)
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(this@MainActivity, Observer {
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


            bannerResult.observe(this@MainActivity, Observer {
                val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
                val viewPagerAdapter = AdsAdapter(this@MainActivity,it!!)
                viewPager.adapter = viewPagerAdapter
            })

            serviceException.observe(this@MainActivity, Observer {
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

    private fun configureUi() {
        add_photo_layout.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        aboutus.setOnClickListener{
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            intent.putExtra("URL", "https://trainingscholar.com/studyapp/about-us.html")
            startActivity(intent)
        }
        feedback.setOnClickListener {
           /* val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            intent.putExtra("URL", "https://trainingscholar.com/studyapp/feedback.html")
            startActivity(intent)*/
            startActivity(Intent(this, LibraryActivity::class.java))

        }
        profileicon.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))

        }



        rcv_cms_tabs.apply {
            layoutManager = GridLayoutManager(context,4)
            adapter = CMSAdapter({

                if(it==0)
                {
                    val intent = Intent(context, SubjectActivity::class.java)
                    intent.putExtra("type", type.toString())
                    intent.putExtra("ID", catid.toString())
                    context.startActivity(intent)
                }
                else if(it==2)
                {
                    Log.e("CHECKDATA","whatsapp is called called")
                    var message = ""
                    var formattedNumber = "+918727888793"
                    formattedNumber = formattedNumber.replace(" ", "").replace("+", "")
                    val sendIntent = Intent("android.intent.action.MAIN")

                    sendIntent.type = "text/plain"
                    sendIntent.putExtra(Intent.EXTRA_TEXT, message)
                    sendIntent.component = ComponentName(
                        "com.whatsapp",
                        "com.whatsapp.Conversation"
                    )
                    sendIntent.putExtra(
                        "jid",
                        PhoneNumberUtils.stripSeparators(formattedNumber) + "@s.whatsapp.net"
                    )
                    context.startActivity(sendIntent)
                }
                else if(it==3)
                {
                    val intent = Intent(context, NotificationActivity::class.java)
                    startActivity(intent)
                }
                else if(it==4)
                {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("URL", "https://trainingscholar.com/studyapp/about-us.html")
                    startActivity(intent)
                }
                else if(it==5)
                {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + "com.rs.roundup" + " APP"
                    )
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                }
                else if(it==6)
                {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("URL", "https://trainingscholar.com/studyapp/feedback.html")
                    startActivity(intent)
                }
                else if(it==7)
                {
                   /* startActivity(Intent(context, WebViewActivity::class.java)
                    )*/
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("URL", "https://trainingscholar.com/studyapp/contact-us.html")
                    startActivity(intent)
                }

            },context)
           // addItemDecoration(GridDividerItemDecoration(0))
        }

        contentVP.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            adapter = VideoThumbsAdapter({

            }, context, emptyList())
            addItemDecoration(VideoItemDecoration(10))
        }
        cam_img.setOnClickListener { startActivity(Intent(this, ScanActivity::class.java)) }
    }

    private fun loadViewPager() {
        val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
     //   val viewPagerAdapter = AdsAdapter(this)
      //  viewPager.adapter = viewPagerAdapter
    }
}
