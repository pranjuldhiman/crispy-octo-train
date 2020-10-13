package com.android.notify

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.roundup.R
import com.android.roundup.models.Notification
import kotlinx.android.synthetic.main.notification_listview.view.*

class NotificationAdapter (private val onSubCateogryClicked:(id: String) -> Unit,
                           private val context: Context,
                           private val subCategoryList: List<Notification>
): RecyclerView.Adapter<NotificationAdapter.SubCategoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        return SubCategoryHolder(LayoutInflater.from(context).inflate(R.layout.notification_listview,parent,false))
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryHolder, position: Int) {
        holder.bind(position,subCategoryList[position])
    }

    inner class SubCategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, notification: Notification){
            itemView.apply {
                setOnClickListener { onSubCateogryClicked(notification.id.toString()) }
                notificationtxt.text = notification.message
            }
        }
    }
}