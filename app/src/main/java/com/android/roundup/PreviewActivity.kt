package com.android.roundup

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.roundup.resultsactivity.ResultsActivity
import com.android.roundup.utils.ImageTextReader
import kotlinx.android.synthetic.main.activity_preview.*

class PreviewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        val imagePath = intent.getStringExtra("imagePath")
        imagePath?.let {
            val bitmap: Bitmap = ImageTextReader.getUprightImage(imagePath)
            img_snap.setImageBitmap(bitmap)
            ImageTextReader.readTextFromImage(bitmap, textView_result)
            btn_continue.setOnClickListener {
                startActivity(Intent(this, ResultsActivity::class.java).putExtra("SearchTag", textView_result.text.toString()))
            }
        }
    }
}
