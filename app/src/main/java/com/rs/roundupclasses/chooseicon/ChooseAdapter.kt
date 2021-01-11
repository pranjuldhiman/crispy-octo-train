package com.rs.roundupclasses.chooseicon

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
import kotlinx.android.synthetic.main.choose_icon_list.view.*

class ChooseAdapter (
    private val onAdapterClicked:(position: String,name: String) -> Unit,
    private val context: Context,
    private val subCategoryList: List<Subcategory>
): RecyclerView.Adapter<ChooseAdapter.SubCategoryHolder>() {
      override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        return SubCategoryHolder(LayoutInflater.from(context).inflate(R.layout.choose_icon_list,parent,false))
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
                setOnClickListener {  onAdapterClicked(subcategorydata.id.toString(),subcategorydata.name.toString())}
                icon_txt.text = subcategorydata.name
                Log.e("CHOOSEICON","subcateogry icons is.........."+subcategorydata.icons)
                RoundUpHelper.setImageUsingPicasso(context, iconimg, RoundUpRepositoryRetrofit.THUMBNAIL_IMAGE_URL.plus(subcategorydata.icons.orEmpty()))

                //   img_cms_logo.setImageDrawable(context.getDrawable(drawableId))
              //  txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}