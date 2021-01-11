package com.rs.roundupclasses.youtube

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.models.Comment
import kotlinx.android.synthetic.main.comment_listview.view.*
import java.util.*

class CommentAdapter  (private val onSubCateogryClicked:(title: String,message: String,time: String) -> Unit,
                       private val context: Context,
                       private val subCategoryList: List<Comment>
): RecyclerView.Adapter<CommentAdapter.SubCategoryHolder>() {

    var list: List<Comment> = ArrayList<Comment>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        return SubCategoryHolder(LayoutInflater.from(context).inflate(R.layout.comment_listview,parent,false))
    }

    override fun getItemCount(): Int {
        list = subCategoryList
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryHolder, position: Int) {
        holder.bind(position,subCategoryList[position])
    }

    fun getCurrentList(): List<Comment?>? {
        return list
    }

    inner class SubCategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, notification: Comment){
            itemView.apply {
              //  setOnClickListener { onSubCateogryClicked(notification.title.toString(),notification.message.toString(),notification.created_datetime.toString()) }
                msg.text = notification.comment
                notificationtime.text = notification.created_datetime
             //   datetime.text = notification.created_datetime
            }
        }
    }
}