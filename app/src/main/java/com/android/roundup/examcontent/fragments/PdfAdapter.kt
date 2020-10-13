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
import kotlinx.android.synthetic.main.item_pdf_list.view.*
import kotlinx.android.synthetic.main.item_pdf_list.view.txt_description
import kotlinx.android.synthetic.main.item_video_list.view.*

class PdfAdapter(private val onAdapterClicked:(name: String,url: String) -> Unit,
                 private val context: Context,
                 private val listofPdf: List<Videos>
): RecyclerView.Adapter<PdfAdapter.CMSHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CMSHolder {
        return CMSHolder(LayoutInflater.from(context).inflate(R.layout.item_pdf_list,parent,false))
    }

    override fun getItemCount(): Int {
        return listofPdf.size
    }

    override fun onBindViewHolder(holder: CMSHolder, position: Int) {
        holder.bind(position,listofPdf[position])
    }

    inner class CMSHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, videosdata: Videos){
            itemView.apply {
                setOnClickListener { onAdapterClicked(videosdata.title.toString(),videosdata.pdfurl.toString()) }
            //    title.text = videosdata.title.toString()
              //  description.text = videosdata.description.toString()

                txt_description.loadData(videosdata.description, "text/html", "UTF-8")

             /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    description.setText(Html.fromHtml(videosdata.description, Html.FROM_HTML_MODE_LEGACY));
                } else
                    description.setText(Html.fromHtml(videosdata.description));*/
               // txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}
