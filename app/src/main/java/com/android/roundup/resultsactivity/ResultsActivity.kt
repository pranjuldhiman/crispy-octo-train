package com.android.roundup.resultsactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.roundup.R
import com.android.roundup.utils.RoundUpHelper

class ResultsActivity: AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, RoundUpHelper.viewModelFactory { ResultsViewModel(this) }).get(ResultsViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.result_activity)
        //configureUI()
        //setViewModelObservers()
    }

    override fun onResume() {
        super.onResume()
        val searchTag = intent.getStringExtra("SearchTag")
        searchTag?.let {
            viewModel.getResultsBySearchTag(searchTag)
        }
    }
}
