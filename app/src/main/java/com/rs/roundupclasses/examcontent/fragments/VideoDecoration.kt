package com.rs.roundupclasses.examcontent.fragments

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class VideoDecoration(private val spaceHeight: Int): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = spaceHeight
            left =  0
            right = 0
            bottom = if (parent.getChildLayoutPosition(view) == parent.childCount-1){
                spaceHeight
            }else{
                0
            }
        }
    }
}
