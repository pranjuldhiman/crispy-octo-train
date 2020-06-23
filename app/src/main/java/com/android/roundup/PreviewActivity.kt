package com.android.roundup

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.roundup.resultsactivity.ResultsActivity
import com.android.roundup.utils.ImageTextReader
import kotlinx.android.synthetic.main.activity_preview.*
import java.io.File
import java.io.FileInputStream


class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val imagePath = intent.getStringExtra("imagePath")
        imagePath?.let {
            val bitmap: Bitmap? = getBitMap(imagePath)
            bitmap?.let {


                img_snap.setImageBitmap(bitmap)
                //ImageTextReader.readTextFromImage(bitmap, textView_result)
                btn_continue.setOnClickListener {
                    startActivity(
                        Intent(this, ResultsActivity::class.java).putExtra(
                            "SearchTag",
                            textView_result.text.toString()
                        )
                    )
                }
            }
        }
    }

    fun getBitMap(path: String): Bitmap? {

        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options: BitmapFactory.Options = BitmapFactory.Options()
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
            img_snap.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap

    }
}
