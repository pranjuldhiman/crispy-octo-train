package com.android.roundup.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.roundup.R
import com.android.roundup.dashboard.model.Subcategory
import com.android.roundup.service.RoundUpRepositoryRetrofit
import com.android.roundup.utils.RoundUpHelper
import kotlinx.android.synthetic.main.item_bank_video_holder.view.*

class ExamVideoAdapter(
    private val onAdapterClicked:(videoUrl: String,name:String) -> Unit,
    private val context: Context,
    private val listOfSubCategory: List<Subcategory>
): RecyclerView.Adapter<ExamVideoAdapter.VideoThumbsHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoThumbsHolder {
        return VideoThumbsHolder(LayoutInflater.from(context).inflate(R.layout.item_bank_video_holder,parent,false))
    }

    override fun getItemCount(): Int {
        return listOfSubCategory.size
    }

    override fun onBindViewHolder(holder: VideoThumbsHolder, position: Int) {
        holder.bind(position,listOfSubCategory[position])
    }

    inner class VideoThumbsHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, subCategory: Subcategory){
            itemView.apply {
                setOnClickListener {
                    onAdapterClicked(subCategory.id.orEmpty(),subCategory.name.toString())
                }
                RoundUpHelper.setImageUsingPicasso(context, img_thumbnail, RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(subCategory.thumbnail_banner.orEmpty()))
            }
        }
    }
}
