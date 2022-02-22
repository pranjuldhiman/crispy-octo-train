package com.rs.roundupclasses.examcontent.fragments

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rs.notify.NotificationAdapter
import com.rs.roundupclasses.R
import com.rs.roundupclasses.dashboard.model.Videos
import com.rs.roundupclasses.models.Notification
import kotlinx.android.synthetic.main.notification_listview.view.*

class DemoAdapter (private val onSubCateogryClicked:(title: String,message: String,time: String) -> Unit,
                   private val context: Context,
                   private val subCategoryList: List<Videos>
): RecyclerView.Adapter<DemoAdapter.SubCategoryHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryHolder {
        return SubCategoryHolder(LayoutInflater.from(context).inflate(R.layout.item_pdf_list,parent,false))
    }

    override fun getItemCount(): Int {
        return subCategoryList.size
    }

    override fun onBindViewHolder(holder: SubCategoryHolder, position: Int) {
        holder.bind(position,subCategoryList[position])
    }

    inner class SubCategoryHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, notification: Videos){
            itemView.apply {
               // setOnClickListener { onSubCateogryClicked(notification.title.toString(),notification.message.toString(),notification.created_datetime.toString()) }
//                title.text = notification.title
                // notificationtxt.text = notification.message

                //   val webview = findViewById<View>(R.id.notificationtxt) as WebView
                //    webview.settings.javaScriptEnabled = true
                //     webview.loadData(notification.message, "text/html; charset=utf-8", "UTF-8")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                 //   notificationtxt.setText(Html.fromHtml(notification.message, Html.FROM_HTML_MODE_COMPACT));
                } else {
                   // notificationtxt.setText(Html.fromHtml(notification.message));
                }
           //     datetime.text = notification.created_datetime
            }
        }
    }
}