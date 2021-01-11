package com.rs.roundupclasses.notify

import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.rs.roundupclasses.R
import kotlinx.android.synthetic.main.notification_detail.*


class NotificationDetail : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notification_detail)
        var titlevalue = intent.getStringExtra("TITLE")!!
        var message = intent.getStringExtra("MESSAGE")!!
        var time = intent.getStringExtra("TIME")!!
        titletxt.setText(titlevalue)

        val data = "Your data which you want to load"
        val webview = findViewById<View>(R.id.notificationtxt) as WebView
        webview.settings.javaScriptEnabled = true
        webview.loadData(message, "text/html; charset=utf-8", "UTF-8")

        datetime.setText(time)

        notification_back_icon.setOnClickListener {
            onBackPressed()
        }
  // 7889169372
        //1726
    }

}