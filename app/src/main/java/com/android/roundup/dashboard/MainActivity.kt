package com.android.roundup.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.R
import com.android.roundup.ScanActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_main)
        configureUi()
    }

    private fun configureUi() {
        add_photo_layout.setOnClickListener {
            startActivity(Intent(this, ScanActivity::class.java))
        }
        cam_img.setOnClickListener { startActivity(Intent(this, ScanActivity::class.java)) }
    }
}
