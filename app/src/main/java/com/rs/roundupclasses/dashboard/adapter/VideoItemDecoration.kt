package com.rs.roundupclasses.dashboard.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

internal class VideoItemDecoration(private val spaceHeight: Int): RecyclerView.ItemDecoration(){
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = spaceHeight
            left =  spaceHeight
            right = spaceHeight
            bottom = spaceHeight
        }
    }
}
