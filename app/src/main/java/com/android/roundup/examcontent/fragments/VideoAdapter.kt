package com.android.roundup.examcontent.fragments

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.roundup.R
import com.android.roundup.dashboard.model.Videos
import com.android.roundup.service.RoundUpRepositoryRetrofit
import com.android.roundup.utils.RoundUpHelper
import kotlinx.android.synthetic.main.item_video_list.view.*
import kotlinx.android.synthetic.main.item_video_list.view.txt_description
import kotlinx.android.synthetic.main.search_item.view.*

class VideoAdapter(
    private val onAdapterClicked:(position: String) -> Unit,
    private val context: Context,
    private val listOfVideos: List<Videos>
): RecyclerView.Adapter<VideoAdapter.CMSHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CMSHolder {
        return CMSHolder(LayoutInflater.from(context).inflate(R.layout.item_video_list,parent,false))
    }

    override fun getItemCount(): Int {
        return listOfVideos.size
    }

    override fun onBindViewHolder(holder: CMSHolder, position: Int) {
        holder.bind(position,listOfVideos[position])
    }

    inner class CMSHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, videosdata: Videos){
            itemView.apply {
                setOnClickListener { onAdapterClicked(videosdata.videourl.toString()) }
              //  RoundUpHelper.setImageUsingPicasso(context, videimg, RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(videosdata.thumbnail.orEmpty()))
                txt_description.loadData(videosdata.description, "text/html", "UTF-8")


                //  img_cms_logo.setImageDrawable(context.getDrawable(drawableId))
               // txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}
