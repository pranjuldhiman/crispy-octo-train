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
import com.rs.roundupclasses.pdf.PdfViewActivity
import kotlinx.android.synthetic.main.item_pdf_list.view.*
import kotlinx.android.synthetic.main.item_pdf_list.view.txt_description

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

                mainpdflayout.setOnClickListener {
                    Log.e("MAINLAYOUT","mainpdflayout mainpdflayout listener is called........")
                    val intent = Intent(context, PdfViewActivity::class.java)
                    intent.putExtra("ViewType", videosdata.title.toString())
                    intent.putExtra("URL", videosdata.pdfurl.toString())
                    context.startActivity(intent)
                }
             /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    description.setText(Html.fromHtml(videosdata.description, Html.FROM_HTML_MODE_LEGACY));
                } else
                    description.setText(Html.fromHtml(videosdata.description));*/
               // txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}
