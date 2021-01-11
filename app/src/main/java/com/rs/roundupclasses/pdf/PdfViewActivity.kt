package com.rs.roundupclasses.pdf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.rs.roundupclasses.R
import com.downloader.Error
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import kotlinx.android.synthetic.main.activity_pdf_view.*
import java.io.File

class PdfViewActivity : AppCompatActivity() {

    companion object {
        private const val PDF_SELECTION_CODE = 99
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)
         var back_icons :ImageView= findViewById(R.id.back_icons)
        PRDownloader.initialize(applicationContext)
        checkPdfAction(intent)
        back_icons.setOnClickListener {
            onBackPressed()
        }

    }


    private fun showPdfFromAssets(pdfName: String) {
        pdfView.fromAsset(pdfName)
            .password(null)
            .defaultPage(0)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@PdfViewActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    private fun selectPdfFromStorage() {
        Toast.makeText(this, "selectPDF", Toast.LENGTH_LONG).show()
        val browseStorage = Intent(Intent.ACTION_GET_CONTENT)
        browseStorage.type = "application/pdf"
        browseStorage.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(
            Intent.createChooser(browseStorage, "Select PDF"), PDF_SELECTION_CODE
        )
    }

    private fun showPdfFromUri(uri: Uri?) {
        pdfView.fromUri(uri)
            .defaultPage(0)
            .spacing(10)
            .load()
    }

    private fun showPdfFromFile(file: File) {
        pdfView.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
                Toast.makeText(
                    this@PdfViewActivity,
                    "Error at page: $page", Toast.LENGTH_LONG
                ).show()
            }
            .load()
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {

        Log.e("MAINLAYOUT","mainlayout click listener is called......."+url.toString())
        PRDownloader.download(
           "http://roundupclasses.com/assets/pdfs/"+ url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                   // Toast.makeText(this@PdfViewActivity, "downloadComplete", Toast.LENGTH_LONG).show()
                    Log.e("CHECKDATA","directory path is....."+dirPath.toString())
                    val downloadedFile = File(dirPath, fileName)
                    progressBar.visibility = View.GONE
                    showPdfFromFile(downloadedFile)
                }
                override fun onError(error: Error?) {
                    Toast.makeText(
                        this@PdfViewActivity,
                        "Error in downloading file : $error",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun checkPdfAction(intent: Intent) {
        progressBar.visibility = View.VISIBLE
        val fileName = "myFile.pdf"
        downloadPdfFromInternet(
            intent.getStringExtra("URL")!!,
            FileUtils.getRootDirPath(this),
            fileName
        )
      /*  when (intent.getStringExtra("ViewType")) {
            "assets" -> {
                showPdfFromAssets(FileUtils.getPdfNameFromAssets())
            }
            "storage" -> {
                selectPdfFromStorage()
            }
            "internet" -> {
                progressBar.visibility = View.VISIBLE
                val fileName = "myFile.pdf"
                downloadPdfFromInternet(
                    FileUtils.getPdfUrl(),
                    FileUtils.getRootDirPath(this),
                    fileName
                )
            }
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_SELECTION_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val selectedPdfFromStorage = data.data
            showPdfFromUri(selectedPdfFromStorage)
        }
    }

}
