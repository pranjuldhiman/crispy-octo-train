package com.android.roundup.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ProgressBar
import com.android.roundup.R

class ProgressDialog(context: Context) {
    private val dialog: Dialog

    init {
        val progressBar = ProgressBar(context)
        dialog = Dialog(context, R.style.ProgressDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(progressBar)
        dialog.setCancelable(false)
    }

    private fun show() {
        if (!dialog.isShowing)
            dialog.show()
    }

    private fun dismiss() {
        if (dialog.isShowing)
            dialog.dismiss()
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading)
            show()
        else
            dismiss()
    }
}
