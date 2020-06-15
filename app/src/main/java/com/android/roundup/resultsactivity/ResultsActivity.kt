package com.android.roundup.resultsactivity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.android.roundup.R
import com.android.roundup.YouTubePlayerActivity
import com.android.roundup.resultsactivity.adapter.ResultsAdapter
import com.android.roundup.utils.RoundUpHelper
import kotlinx.android.synthetic.main.result_activity.*

class ResultsActivity: AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, RoundUpHelper.viewModelFactory { ResultsViewModel(this) }).get(ResultsViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        setViewModelObservers()
        img_back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(this@ResultsActivity, Observer {
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
