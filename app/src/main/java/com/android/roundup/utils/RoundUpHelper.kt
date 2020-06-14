package com.android.roundup.utils

import android.util.Log
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
    }
}
