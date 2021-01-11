package com.rs.roundupclasses.examcontent.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.youtube.YouTubePlayerActivity
import com.rs.roundupclasses.dashboard.adapter.VideoItemDecoration
import com.rs.roundupclasses.utils.ApiStatus
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.ProgressDialog
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_video_list.*

class VideoFragment: Fragment() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit  var progress: ProgressDialog;

    private val viewModel by lazy {
        ViewModelProviders.of(this, RoundUpHelper.viewModelFactory { VideoFragmentModel(context!!) }).get(
            VideoFragmentModel::class.java)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sharedPreferences = (activity as AppCompatActivity).getSharedPreferences(Constants.SHAREDPREFERENCEFILE, Context.MODE_PRIVATE)
        var topic_id = sharedPreferences.getString(Constants.TOPICID,"")
        var userid: String= sharedPreferences.getString(Constants.USERID,"").toString()
        progress = ProgressDialog(activity as AppCompatActivity)

        viewModel.getVideoData(userid,topic_id!!)
        setViewModelObservers()
        /*rcv_exam_component.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = VideoAdapter({

            }, context, emptyList())
            addItemDecoration(VideoDecoration(40))
        }*/
    }

    private fun setViewModelObservers() {
        viewModel.apply {
            listOfSearchResult.observe(viewLifecycleOwner, Observer {
                video_list.visibility = View.VISIBLE
                video_list.apply {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = VideoAdapter({url: String, id: String ->
                     //   startActivity((activity as AppCompatActivity).Intent(this@VideoFragment, YouTubePlayerActivity::class.java).putExtra("videoUrl", it.videourl))
                        val intent = Intent(context, YouTubePlayerActivity::class.java)
                        intent.putExtra("videoUrl", url)
                        intent.putExtra("ID", id)
                        context.startActivity(intent)
                    }, context, it.orEmpty())
                   addItemDecoration(VideoItemDecoration(10))
                }
            })
            serviceException.observe(viewLifecycleOwner, Observer {
                rcv_dashboard.visibility = View.GONE
                showMessageDialog(it.orEmpty())
            })

            viewModel.status.observe(viewLifecycleOwner, Observer {
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
        }
    }

    private fun showMessageDialog(msgWrapper: String?) {
        msgWrapper?.let {
            val msg = it
            AlertDialog.Builder(activity as AppCompatActivity)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    //startActivity(Intent(this, ScanActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
                .create().run {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }
}
