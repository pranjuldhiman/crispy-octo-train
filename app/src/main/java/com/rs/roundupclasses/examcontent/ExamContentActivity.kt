package com.rs.roundupclasses.examcontent

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.rs.roundupclasses.R
import com.google.android.material.tabs.TabLayout

class ExamContentActivity: AppCompatActivity() {
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
