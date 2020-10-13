package com.android.roundup.webview

import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.R

class WebViewActivity : AppCompatActivity()
{

    var progressBar: ProgressBar? = null
    var back_icon: ImageView? = null
    //    internal var url = "http://facebook.com"
    var url = ""
    var type = ""
    var webView: WebView? = null
    var toolbartext: TextView? = null
    var wv: WebView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.webviewactivity)
        url = intent.getStringExtra("URL")!!
        Log.e("WEBVIEW","Webview is called......"+url)
        //  val id = SubCategoryViewArgs.fromBundle(arguments!!).id
        wv = findViewById<WebView>(R.id.webviewlayout)
        progressBar =findViewById<ProgressBar>(R.id.progressBar)
       //ch back_icon = findViewById<ImageView>(R.id.back_icon)
       //ch toolbartext = findViewById<TextView>(R.id.toolbartext)
        //   wv.webViewClient = myWebClient()
        wv!!.settings.javaScriptEnabled = true
        wv!!.settings.builtInZoomControls = true
        wv!!.settings.displayZoomControls = false
        wv!!.loadUrl(url)

        wv!!.setWebViewClient(object : WebViewClient() {

            //            override fun shouldOverrideUrlLoading(view: WebView?,request: WebResourceRequest?): Boolean {
//                progressBar!!.visibility = View.VISIBLE
//                view!!.loadUrl(url)
//                return true
//            }
            override fun onPageFinished(view: WebView, url: String) {
                progressBar!!.visibility = View.GONE
            }
        })

       /* back_icon!!.setOnClickListener {

        }*/

    }

}