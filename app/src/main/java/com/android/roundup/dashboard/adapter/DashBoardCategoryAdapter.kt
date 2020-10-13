package com.android.roundup.dashboard.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.roundup.R
import com.android.roundup.dashboard.model.Data
import com.android.roundup.dashboard.model.Subcategory
import com.android.roundup.subjectlist.SubjectActivity
import kotlinx.android.synthetic.main.item_dashboard_category.view.*

class DashBoardCategoryAdapter(
    private val onCategoryClicked:(havingSubCategory: Data) -> Unit,
    private val context: Context,
    private val categoryDataList: List<Data>
): RecyclerView.Adapter<DashBoardCategoryAdapter.CategoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(LayoutInflater.from(context).inflate(R.layout.item_dashboard_category,parent,false))
    }

    override fun getItemCount(): Int {
        return categoryDataList.size
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(position,categoryDataList[position])
    }

    inner class CategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, categoryData: Data){
            itemView.apply {
                txt_category_name.text = categoryData.category.toString()
                txt_category_all.setOnClickListener {
                    onCategoryClicked(categoryDataList.get(position)!!)
                }
                rcv_category_video.apply {

                    var subcategory: MutableList<Subcategory>? =  mutableListOf()

                    for(data in categoryData.subcategory.orEmpty().indices)
                    {
                        if(data<3)
                        {
                            subcategory!!.add(Subcategory(categoryData.subcategory!!.get(data).category_id,categoryData.subcategory.get(data).created_datetime,categoryData.subcategory.get(data).icons,categoryData.subcategory.get(data).id,
                                categoryData.subcategory.get(data).is_active,categoryData.subcategory.get(data).name,categoryData.subcategory.get(data).thumbnail_banner,""))
                        }
                    }



                    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    adapter = ExamVideoAdapter({ s: String, s1: String ->
                        if(s1.equals("1"))
                        {
                           // context.startActivity(Intent(context, ExamContentActivity::class.java))
                         // startActivity(Intent(context, YouTubePlayerActivity::class.java).putExtra("videoUrl", ""))
                              val intent = Intent(context, SubjectActivity::class.java)
                             intent.putExtra("type"," ")
                             intent.putExtra("ID", s)
                             context.startActivity(intent)
                        }
                        else
                        {
                            val intent = Intent(context, SubjectActivity::class.java)
                            intent.putExtra("type", s1)
                            intent.putExtra("ID", s)
                            context.startActivity(intent)
                        //    context.startActivity(Intent(context, ExamContentActivity::class.java))
                        }
                 //   }, context, categoryData.subcategory.orEmpty())
                    }, context, subcategory.orEmpty())
                    addItemDecoration(VideoItemDecoration(5))
                }
               // RoundUpHelper.setImageUsingPicasso(context, img_video_thumbnail, searchRes)
            }
        }
    }
}
