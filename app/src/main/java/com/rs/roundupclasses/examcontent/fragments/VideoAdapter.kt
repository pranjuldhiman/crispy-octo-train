package com.rs.roundupclasses.examcontent.fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.model.Videos
import com.rs.roundupclasses.youtube.YouTubePlayerActivity
import kotlinx.android.synthetic.main.item_video_list.view.*
import kotlinx.android.synthetic.main.item_video_list.view.txt_description

class VideoAdapter(
    private val onAdapterClicked:(position: String,id:String) -> Unit,
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
                setOnClickListener {
                    Log.e("VIDEOADAPTER","video adapter is called")

                    onAdapterClicked(videosdata.videourl.toString(),videosdata.id.toString()) }
              //  RoundUpHelper.setImageUsingPicasso(context, videimg, RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(videosdata.thumbnail.orEmpty()))

               txt_description.loadData(videosdata.description!!, "text/html", "UTF-8")
                Log.e("VIDEOADAPTER","txt_description")



                txt_description.setOnClickListener {
                   val intent = Intent(context, YouTubePlayerActivity::class.java)
                    intent.putExtra("videoUrl", videosdata.videourl.toString())
                    intent.putExtra("ID", videosdata.id.toString())
                    context.startActivity(intent)
                    Log.e("VIDEOADAPTER","vidthumbnail is called")
                }


                mainlayout.setOnClickListener {
                    Log.e("VIDEOADAPTER","video adapter is called")
                }

                democlick.setOnClickListener {
                    Log.e("VIDEOADAPTER","video adapter is called")
                    Log.e("VIDEOADAPTER","video adapter is called")
                    val intent = Intent(context, YouTubePlayerActivity::class.java)
                    intent.putExtra("videoUrl", videosdata.videourl.toString())
                    intent.putExtra("ID", videosdata.id.toString())
                    context.startActivity(intent)

                }
                //  img_cms_logo.setImageDrawable(context.getDrawable(drawableId))
               // txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}
