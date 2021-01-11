package com.rs.roundupclasses.aftersubbjectlist

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.model.Subcategory
import com.rs.roundupclasses.service.RoundUpRepositoryRetrofit
import com.rs.roundupclasses.utils.RoundUpHelper
import kotlinx.android.synthetic.main.subject_item_list.view.*

class AfterSubjectListAdapter  (
    private val onSubCateogryClicked:(havingSubCategory: String) -> Unit,
    private val context: Context,
    private val subCategoryList: List<Subcategory>
): RecyclerView.Adapter<AfterSubjectListAdapter.SubCategoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        return SubCategoryHolder(LayoutInflater.from(context).inflate(R.layout.subject_item_list,parent,false))
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryHolder, position: Int) {
        holder.bind(position,subCategoryList[position])
    }

    inner class SubCategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, subcategorydata: Subcategory){
            itemView.apply {
                setOnClickListener { subcategorydata.id }
                txt_head_line.text = subcategorydata.name

//error(R.drawable.dra). .resize(110, 110).centerCrop()
                Log.e("CHECKDATA","subcategory image is......"+subcategorydata.thumbnail)
                // Picasso.with(context).load("https://trainingscholar.com/studyapp/assets/images/thumbnails/"+subcategorydata.thumbnail).into(mainimg);
                // Picasso.with(context).load(RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(subcategorydata.thumbnail_banner.orEmpty())).into(mainimg);
                RoundUpHelper.setImageUsingPicasso(context, mainimg, RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(subcategorydata.thumbnail.orEmpty()))

                //  icon_txt.text = subcategorydata.name
                // RoundUpHelper.setImageUsingPicasso(context, iconimg, RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(subcategorydata.icons.orEmpty()))

                //   img_cms_logo.setImageDrawable(context.getDrawable(drawableId))
                //  txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}