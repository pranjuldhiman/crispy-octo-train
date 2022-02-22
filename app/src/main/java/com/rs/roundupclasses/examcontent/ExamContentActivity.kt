package com.rs.roundupclasses.examcontent

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.rs.roundupclasses.R
import com.google.android.material.tabs.TabLayout
import com.rs.notify.NotificationAdapter
import com.rs.roundupclasses.dashboard.adapter.VideoItemDecoration
import com.rs.roundupclasses.examcontent.fragments.PdfAdapter
import com.rs.roundupclasses.examcontent.fragments.PdfFragmentViewModel
import com.rs.roundupclasses.pdf.PdfViewActivity
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_pdf_list.*

class ExamContentActivity: AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { PdfFragmentViewModel(this) }).get(
            PdfFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_exam_content)
       var toolbartext = findViewById<TextView>(R.id.toolbartex);
        var back_icons = findViewById<ImageView>(R.id.back_icons);
        toolbartext.text=intent.getStringExtra("type")
       back_icons.setOnClickListener {
            onBackPressed()
        }
      configureUI()
    }


   private fun configureUI() {
        val tabAdapter = ExamPagerAdapter(this,supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
        val jobViewPager = findViewById<ViewPager>(R.id.viewPagerExamContent)
        jobViewPager.adapter = tabAdapter

        findViewById<TabLayout>(R.id.tabLayout).apply {
            setupWithViewPager(jobViewPager)
        }
    }
}
