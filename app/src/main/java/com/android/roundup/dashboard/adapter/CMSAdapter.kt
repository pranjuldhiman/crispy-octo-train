package com.android.roundup.dashboard.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.roundup.R
import kotlinx.android.synthetic.main.item_cms_holder.view.*

class CMSAdapter(
    private val onAdapterClicked:(position: Int) -> Unit,
    private val context: Context
): RecyclerView.Adapter<CMSAdapter.CMSHolder>() {
    private val iconList = listOf(R.drawable.pdf, R.drawable.video, R.drawable.whatsapp, R.drawable.update, R.drawable.info, R.drawable.share, R.drawable.feedback, R.drawable.contact)
    private val nameList = listOf(R.string.pdf, R.string.video, R.string.whatsapp, R.string.update, R.string.aboutus, R.string.share, R.string.feedback, R.string.contact)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CMSHolder {
        return CMSHolder(LayoutInflater.from(context).inflate(R.layout.item_cms_holder,parent,false))
    }

    override fun getItemCount(): Int {
        return iconList.size
    }

    override fun onBindViewHolder(holder: CMSHolder, position: Int) {
        holder.bind(position,iconList[position], nameList[position])
    }

    inner class CMSHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int, drawableId: Int, nameId: Int){
            itemView.apply {
                setOnClickListener { onAdapterClicked(position) }
                img_cms_logo.setImageDrawable(context.getDrawable(drawableId))
                txt_cms_name.text = context.getText(nameId)
            }
        }
    }
}
