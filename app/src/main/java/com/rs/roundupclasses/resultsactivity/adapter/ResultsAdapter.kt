package com.rs.roundupclasses.resultsactivity.adapter

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rs.roundupclasses.R
import com.rs.roundupclasses.models.Data
import com.rs.roundupclasses.utils.PicassoImageGetter
import com.rs.roundupclasses.utils.RoundUpHelper
import com.rs.roundupclasses.utils.URLImageParser
import kotlinx.android.synthetic.main.search_item.view.*


class ResultsAdapter(
    private val onAdapterClicked:(vehicle: Data) -> Unit,
    private val context: Context,
    private val listOfResults: List<Data>
): RecyclerView.Adapter<ResultsAdapter.ResultHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        return ResultHolder(LayoutInflater.from(context).inflate(R.layout.search_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listOfResults.size
    }

    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        holder.bind(position,listOfResults[position])
    }

    inner class ResultHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, searchRes: Data){
            itemView.apply {
                thumbnail.setOnClickListener { onAdapterClicked(searchRes) }
                val bgColor = RoundUpHelper.getRandomColor()
                frame_thumbnail.foreground = ColorDrawable(bgColor)
                txt_description.loadData(searchRes.description, "text/html", "UTF-8")
            }
        }

        private fun setHtmlText(view: TextView, tvContent: String){
            val imageGetter = PicassoImageGetter(context, view)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.text = Html.fromHtml(tvContent, Html.FROM_HTML_MODE_LEGACY, URLImageParser(view, context), null)
            } else {
                view.text = Html.fromHtml(tvContent, URLImageParser(view, context), null);
            }
        }
    }
}
