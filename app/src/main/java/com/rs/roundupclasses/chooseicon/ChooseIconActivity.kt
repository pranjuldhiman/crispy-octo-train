package com.rs.roundupclasses.chooseicon

import GridDividerItemDecoration
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.model.Subcategory
import com.rs.roundupclasses.scan.ScanActivity
import com.rs.roundupclasses.subjectlist.SubjectActivity
import com.rs.roundupclasses.utils.ApiStatus
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.ProgressDialog
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.chooseicon_activity.*


class ChooseIconActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit  var progress: ProgressDialog;


    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { ChooseIconViewModel(this) }).get(
            ChooseIconViewModel::class.java)
    }
    lateinit  var list:ArrayList<Subcategory>;
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

        setContentView(R.layout.chooseicon_activity)
        var type = intent.getStringExtra("type")
        toolbartext.text = type.toString()
        list = intent.getSerializableExtra("LIST") as ArrayList<Subcategory>
        Log.e("CHECKDATA","category list is....."+list.size);
        progress = ProgressDialog(this)

        choose_icon.apply {
            layoutManager = GridLayoutManager(context,4)
            adapter = ChooseAdapter({ s: String, s1: String ->
                val intent = Intent(context, SubjectActivity::class.java)
                intent.putExtra("type", s1)
                intent.putExtra("ID", s)
                context.startActivity(intent)
            },context,list.orEmpty())
            addItemDecoration(GridDividerItemDecoration(3))
        }

        back_icon.setOnClickListener {
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

      //  setViewModelObservers()
    }

    // view all 1
    // 0 pdf videos
}