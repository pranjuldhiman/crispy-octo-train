package com.rs.roundupclasses.resultsactivity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rs.roundupclasses.R
import com.rs.roundupclasses.scan.ScanActivity
import com.rs.roundupclasses.resultsactivity.adapter.ResultsAdapter
import com.rs.roundupclasses.utils.ImageTextReader
import com.rs.roundupclasses.utils.RoundUpHelper
import com.rs.roundupclasses.youtube.YouTubePlayerActivity
import kotlinx.android.synthetic.main.result_activity.*
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
                img_snap.visibility = View.VISIBLE
                img_snap.setImageBitmap(bitmap)
                ImageTextReader.readTextFromImage(bitmap, textView_results)
                textView_results.addTextChangedListener {
                    viewModel.getResultsBySearchTag(it.toString())
                }
                val searchTag = intent?.getStringExtra("SearchTag")
                searchTag?.let {
                    img_snap.visibility = View.GONE
                    viewModel.getResultsBySearchTag(searchTag)
                }
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
                        startActivity(Intent(this@ResultsActivity, YouTubePlayerActivity::class.java).putExtra("videoUrl", it.videourl).putExtra("ID", it.id))
                    },this@ResultsActivity, it.orEmpty())
                }
            })

            serviceException.observe(this@ResultsActivity, Observer {
                progress_bar.visibility = View.INVISIBLE
                rcv_results.visibility = View.GONE
                showMessageDialog(it.orEmpty())
            })
        }
    }

    override fun onResume() {
        super.onResume()
        val searchTag = intent.getStringExtra("SearchTag")
        searchTag?.let {
            img_snap.visibility = View.GONE
            viewModel.getResultsBySearchTag(searchTag)
        }
    }

    private fun showMessageDialog(msgWrapper: String?) {
        msgWrapper?.let {
            val msg = it
            AlertDialog.Builder(this)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    startActivity(Intent(this, ScanActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                .create().run {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }
}
