package com.rs.roundupclasses.dashboard

import android.Manifest
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.rs.roundupclasses.R
import com.rs.roundupclasses.chooseicon.ChooseIconActivity
import com.rs.roundupclasses.dashboard.adapter.*
import com.rs.roundupclasses.dashboard.model.Subcategory
import com.rs.roundupclasses.examcontent.ExamContentActivity
import com.rs.roundupclasses.library.LibraryActivity
import com.rs.roundupclasses.notify.NotificationActivity
import com.rs.roundupclasses.profile.ProfileActivity
import com.rs.roundupclasses.scan.ScanActivity
import com.rs.roundupclasses.utils.ApiStatus
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.ProgressDialog
import com.rs.roundupclasses.utils.RoundUpHelper
import com.rs.roundupclasses.webview.Feedback
import com.rs.roundupclasses.webview.WebViewActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    lateinit var progress: ProgressDialog;
    lateinit var sharedPreferences: SharedPreferences
    lateinit var type: String
    lateinit var catid: String

    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { MainViewModel(this) })
            .get(MainViewModel::class.java)
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
        sharedPreferences =
            this.getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        var userid: String = sharedPreferences.getString(Constants.USERID, "").toString()
        Log.e("SHAREDPREFERENCE", "userid is....." + userid)
        setContentView(R.layout.activity_main)
        progress = ProgressDialog(this)
        configureUi()
        //   loadViewPager()
        getDashboardData(userid)
        setViewModelObservers()
        distance(30.7387, 76.7807, 30.7088, 76.8384)

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
        Log.e("LATLONG", "distance is......" + dist)

        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }


    private fun getDashboardData(userid: String) {
        viewModel.getDashboardData(userid)
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(this@MainActivity, Observer {
                var value: Boolean = true

                for (data in it!!.indices) {

                    if (it.get(data).subcategory!!.size > 0) {
                        for (datas in it.get(data).subcategory!!.indices) {
                            catid = it.get(data).subcategory!!.get(datas).id.toString()
                            type = it.get(data).subcategory!!.get(datas).name.toString()
                            break
                        }
                        Log.e("CATEGORYNAME", "categoryname name name name is......" + type)
                        Log.e("CATEGORYNAME", "categoryname id id id id is......" + catid)

                        Log.e("CATEGORYNAME", "categoryname is......" + it.get(data).category)
                        break
                    } else {
                        Log.e(
                            "CATEGORYNAME",
                            "categoryname else else is......" + it.get(data).category
                        )
                    }
                }



                rcv_dashboard.visibility = View.VISIBLE
                rcv_dashboard.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = DashBoardCategoryAdapter({
                        if (it.having_subcategory.equals("1")) {
                            var list: ArrayList<Subcategory> =
                                it.subcategory as ArrayList<Subcategory>
                            val intent = Intent(context, ChooseIconActivity::class.java)
                            intent.putExtra("type", it.category)
                            intent.putExtra("LIST", list)
                            startActivity(intent)
                        } else {
                            sharedPreferences.edit { putString(Constants.TOPICID, "ONE") }
                            val intent = Intent(context, ExamContentActivity::class.java)
                            intent.putExtra("type", it.category)
                            context.startActivity(intent)
                        }
                    }, context, it.orEmpty())
                    addItemDecoration(VideoItemDecoration(0))
                }
            })

            bannerResult.observe(this@MainActivity, Observer {
                val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
                val viewPagerAdapter = AdsAdapter(this@MainActivity, it!!)
                viewPager.adapter = viewPagerAdapter
            })

            appid.observe(this@MainActivity, Observer {
                sharedPreferences.edit { putString(Constants.APPID, it.toString()) }

            })

            appkey.observe(this@MainActivity, Observer {
                sharedPreferences.edit { putString(Constants.APPKEY, it.toString()) }

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

    private fun isAppInstalled(packageName: String): Boolean {
        val pm = packageManager
        val app_installed: Boolean = try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
        return app_installed
    }

    private fun configureUi() {
        add_photo_layout.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }

        aboutus.setOnClickListener {
            val intent = Intent(this@MainActivity, WebViewActivity::class.java)
            intent.putExtra("type", "About Us")
            intent.putExtra("URL", "http://roundupclasses.com/about-us.html")
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
            layoutManager = GridLayoutManager(context, 4)
            adapter = CMSAdapter({

                if (it == 0) {

                    sharedPreferences.edit { putString(Constants.TOPICID, "ONE") }

                    val intent = Intent(context, ExamContentActivity::class.java)
                    intent.putExtra("type", "Test")
                    context.startActivity(intent)

                    /* val intent = Intent(context, SubjectActivity::class.java)
                     intent.putExtra("type", type.toString())
                     intent.putExtra("ID", catid.toString())
                     context.startActivity(intent)*/
                } else if (it == 1) {
                    startActivity(Intent(context, LibraryActivity::class.java))
                    //          val intent = Intent(context, VideoActivity::class.java)
                    //      context.startActivity(intent)
                } else if (it == 2) {
                    Log.e("CHECKDATA", "whatsapp is called called")

                    var formattedNumber = "+919877163246"
                    if (isAppInstalled("com.whatsapp")) {
                        var message = ""
                        //    var formattedNumber = "+918727888793"
                        //   var formattedNumber = "+917347444327"
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
                    } else {
                        val uri: Uri =
                            Uri.parse("https://api.whatsapp.com/send?phone=$formattedNumber&text=hii!")
                        val sendIntent = Intent(Intent.ACTION_VIEW, uri)
                        context.startActivity(sendIntent)
                    }

                } else if (it == 3) {
                    val intent = Intent(context, NotificationActivity::class.java)
                    startActivity(intent)
                } else if (it == 4) {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("type", "About Us")
                    intent.putExtra("URL", "http://roundupclasses.com/about-us.html")
                    startActivity(intent)
                } else if (it == 5) {
                    val sendIntent = Intent()
                    sendIntent.action = Intent.ACTION_SEND
                    sendIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        "Hi friends i am using ." + " http://play.google.com/store/apps/details?id=" + "com.rs.roundupclasses" + " APP"
                    )

                    //AIzaSyDLqJSj1aoL7kLF44jUkB9GoEoYyn68u3U
                    sendIntent.type = "text/plain"
                    startActivity(sendIntent)
                } else if (it == 6) {

                    askForPermission(
                        Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE
                    )

                    /*   val intent = Intent(context, Feedback::class.java)
                    //   intent.putExtra("type", "FeedBack")
                     //  intent.putExtra("URL", "http://roundupclasses.com/feedback.html")
                       startActivity(intent)*/
                } else if (it == 7) {
                    val intent = Intent(context, WebViewActivity::class.java)
                    intent.putExtra("type", "Contact Us")
                    intent.putExtra("URL", "http://roundupclasses.com/contact-us.html")
                    startActivity(intent)
                }
            }, context)
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

    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(permission),
                requestCode
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        this@MainActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent = Intent(this, Feedback::class.java)
                        //   intent.putExtra("type", "FeedBack")
                        //  intent.putExtra("URL", "http://roundupclasses.com/feedback.html")
                        startActivity(intent)
                    } else {
                        askForPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            WRITE_CAMERA_PERMISSION_CODE
                        )
                    }
                } else {
                    askForPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        READ_EXTERNAL_PERMISSION_CODE
                    )
                }
            } else {
                askForPermission(
                    Manifest.permission.CAMERA,
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                this,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> askForPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_EXTERNAL_PERMISSION_CODE
                )
                READ_EXTERNAL_PERMISSION_CODE -> askForPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    WRITE_CAMERA_PERMISSION_CODE
                )
                WRITE_CAMERA_PERMISSION_CODE -> startCameraSource()
                else -> {
                }
            }
        }
    }

    fun startCameraSource() {
        val intent = Intent(this, Feedback::class.java)
        startActivity(intent)
    }

    companion object {
        private val TAG = ScanActivity::class.java.simpleName
        private const val requestPermissionID = 100
        private const val CAMERA_PERMISSION_CODE = 200
        private const val READ_EXTERNAL_PERMISSION_CODE = 300
        private const val WRITE_CAMERA_PERMISSION_CODE = 400
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }

    private fun loadViewPager() {
        val viewPager = findViewById<View>(R.id.viewPager) as ViewPager
        //   val viewPagerAdapter = AdsAdapter(this)
        //  viewPager.adapter = viewPagerAdapter
    }
}
