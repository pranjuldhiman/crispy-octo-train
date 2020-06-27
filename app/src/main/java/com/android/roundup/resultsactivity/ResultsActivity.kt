package com.android.roundup.resultsactivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.roundup.R
import com.android.roundup.YouTubePlayerActivity
import com.android.roundup.resultsactivity.adapter.ResultsAdapter
import com.android.roundup.utils.ImageTextReader
import com.android.roundup.utils.RoundUpHelper
import kotlinx.android.synthetic.main.activity_preview.*
import kotlinx.android.synthetic.main.result_activity.*
import kotlinx.android.synthetic.main.result_activity.img_snap
import java.io.File
import java.io.FileInputStream

class ResultsActivity: AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, RoundUpHelper.viewModelFactory { ResultsViewModel(this) }).get(ResultsViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        progress_bar.visibility = View.VISIBLE
        rcv_results.visibility = View.INVISIBLE
        setViewModelObservers()
        img_back.setOnClickListener {
            onBackPressed()
        }
        val imagePath = intent.getStringExtra("imagePath")
        imagePath?.let {
            val bitmap: Bitmap? = getBitMap(imagePath)
            bitmap?.let {
                img_snap.setImageBitmap(bitmap)
                ImageTextReader.readTextFromImage(bitmap, textView_results)
                textView_results.addTextChangedListener {
                    viewModel.getResultsBySearchTag(it.toString())
                }

                val searchTag = intent?.getStringExtra("SearchTag")
                searchTag?.let { viewModel.getResultsBySearchTag(searchTag) }
                /*btn_continue.setOnClickListener {
                    startActivity(
                        Intent(this, ResultsActivity::class.java).putExtra(
                            "SearchTag",
                            textView_result.text.toString()
                        )
                    )
                }*/
            }
        }
    }

    fun getBitMap(path: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            img_snap.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap

    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(this@ResultsActivity, Observer {
                progress_bar.visibility = View.INVISIBLE
                rcv_results.visibility = View.VISIBLE
                rcv_results.apply {
                    adapter = ResultsAdapter({
                        startActivity(Intent(this@ResultsActivity, YouTubePlayerActivity::class.java).putExtra("videoUrl", it.videourl))
                    },this@ResultsActivity, it.orEmpty())
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val searchTag = intent.getStringExtra("SearchTag")
        searchTag?.let {
            viewModel.getResultsBySearchTag(searchTag)
        }
    }
}
