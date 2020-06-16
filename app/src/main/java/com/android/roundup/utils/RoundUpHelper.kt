package com.android.roundup.utils

import android.content.Context
import android.graphics.Color
import android.nfc.tech.NfcV.get
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.roundup.R
import com.squareup.picasso.Picasso
import okhttp3.Request
import java.io.IOException
import java.util.*

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

        fun getRandomColor(): Int{
            val rnd = Random()
            return Color.argb(rnd.nextInt(100), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }

        fun makeToast(context: Context, message: String){
            Toast.makeText(context,message, Toast.LENGTH_SHORT).show()
        }

        fun setImageUsingPicasso(context: Context, view: ImageView, url: String){
            Picasso.with(context).load(url).placeholder(R.mipmap.ic_launcher).noFade().into(view);
        }

        fun setImageUsingPicassoWithPlaceHolder(context: Context, view: ImageView, url: String, placeHolder: Int){
            Picasso.with(context).load(url).placeholder(placeHolder).noFade().into(view);
        }
    }
}
