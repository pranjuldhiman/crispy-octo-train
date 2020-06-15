package com.android.roundup.utils

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import okhttp3.Request
import java.io.IOException

class RoundUpHelper {
    companion object{
        fun stringifyRequestBody(request: Request): String{
            if (request.body != null){
                try {
                    val copy = request.newBuilder().build()
                    val buffer = okio.Buffer()
                    copy.body!!.writeTo(buffer)
                    return buffer.readUtf8().replace("\\","");
                } catch (e: IOException) {
                    Log.w("GLOBAL_TAG", "Failed to stringify request body: " + e.message)
                }
            }
            return ""
        }

        // To Create ViewModel instance
        inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
            }
    }
}
