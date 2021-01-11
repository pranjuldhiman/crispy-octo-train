package com.rs.roundupclasses.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rs.roundupclasses.R
import com.rs.roundupclasses.resultsactivity.ResultsActivity
import com.rs.roundupclasses.utils.Constants
import com.rs.roundupclasses.utils.RoundUpHelper.Companion.convertImageFileToBase64
import com.rs.roundupclasses.utils.RoundUpHelper.Companion.getPath
import com.rs.roundupclasses.utils.RoundUpHelper.Companion.viewModelFactory
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.File
import java.util.*

class ScanActivity : AppCompatActivity() {
    private var mRlSearchView: RelativeLayout? = null
    private var action_bar: RelativeLayout? = null
    private var mRlCameraView: FrameLayout? = null
    private var mTextView: TextView? = null
    private var btn_done: TextView? = null
    private var mCameraView: SurfaceView? = null
    private var mCamera: ImageView? = null
    private var img_back: ImageView? = null
    private var mBackBtn: Button? = null
    private var mCaptureBtn: Button? = null
    private var mSubmitBtn: Button? = null
    private var mSearchText: EditText? = null
    private var viewModel: ScanViewModel? = null
    var sharedPreferences: SharedPreferences? = null
    var appid:  String? = ""
    var appkey: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory { ScanViewModel() }
        ).get(
            ScanViewModel::class.java
        )
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(
                this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                false
            )
            window.statusBarColor = Color.TRANSPARENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
        setContentView(R.layout.search_activity)
        sharedPreferences = getSharedPreferences(
            Constants.SHAREDPREFERENCEFILE,
            Context.MODE_PRIVATE
        )
        appid = sharedPreferences!!.getString(Constants.APPID, "")
        appkey = sharedPreferences!!.getString(Constants.APPKEY, "")

        Log.e("MAINSHAREDDATA","mainappid is......"+appid)
        Log.e("MAINSHAREDDATA","appkey is......"+appkey)

        mRlCameraView = findViewById(R.id.rlCameraView)
        mRlSearchView = findViewById(R.id.rlSearchView)
        mCamera = findViewById(R.id.cam)
        mTextView = findViewById(R.id.text_view)
        mCameraView = findViewById(R.id.surfaceView)
        mBackBtn = findViewById(R.id.back_button)
        mCaptureBtn = findViewById(R.id.capture_button)
        mSubmitBtn = findViewById(R.id.submit_button)
        btn_done = findViewById(R.id.btn_done)
        mSearchText = findViewById(R.id.search_text)
        action_bar = findViewById(R.id.action_bar)
        img_back = findViewById(R.id.img_back)

        mCamera!!.setOnClickListener(View.OnClickListener { v: View? ->
            askForPermission(
                Manifest.permission.CAMERA,
                CAMERA_PERMISSION_CODE
            )
        })

        img_back!!.setOnClickListener(View.OnClickListener { v: View? ->
            mRlSearchView!!.setVisibility(View.VISIBLE)
            mRlCameraView!!.setVisibility(View.GONE)
            action_bar!!.setVisibility(View.GONE)
        })
        mCaptureBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val i = Intent(this@ScanActivity, ResultsActivity::class.java)
            i.putExtra("SearchTag", mTextView!!.getText().toString())
            startActivity(i)
        })
        mSubmitBtn!!.setOnClickListener(View.OnClickListener { v: View? ->
            val i = Intent(this@ScanActivity, ResultsActivity::class.java)
            i.putExtra("SearchTag", mSearchText!!.getText().toString())
            startActivity(i)
        })
        btn_done!!.setOnClickListener(View.OnClickListener { view: View? ->
            val i = Intent(this@ScanActivity, ResultsActivity::class.java)
            i.putExtra("SearchTag", mTextView!!.getText().toString())
            startActivity(i)
        })
       // viewModel!!.isSafeCall()
        setViewModelObservers()
    }

    private fun setViewModelObservers() {
        viewModel!!.serviceException.observe(
            this@ScanActivity,
            Observer { msg ->
                AlertDialog.Builder(this@ScanActivity)
                    .setMessage(msg)
                    .setPositiveButton(
                        android.R.string.ok
                    ) { dialogInterface, i -> dialogInterface.dismiss() }
                    .setCancelable(false)
                    .create()
                    .show()
            })
        viewModel!!.searchText.observe(
            this@ScanActivity,
            Observer { searchText ->
                startActivity(
                    Intent(
                        this@ScanActivity,
                        ResultsActivity::class.java
                    ).putExtra("SearchTag", searchText)
                )
            })
        viewModel!!.isSafe.observe(this@ScanActivity, Observer { integer -> val x = 7 / integer })
    }

    private fun startCameraSource() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this)
    }

    @Throws(Exception::class)
    private fun createTempFile(picture: String, s: String): File {
        val tempDir = File(filesDir, "images")
        //tempDir = new File(tempDir.getAbsolutePath() + "/RoundUp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        return File.createTempFile(picture, s, tempDir)
    }

    @Throws(Exception::class)
    private fun createTemporaryFile(picture: String, s: String): File {
        var tempDir = Environment.getExternalStorageDirectory()
        tempDir = File(tempDir.absolutePath + "/RoundUp/")
        if (!tempDir.exists()) {
            tempDir.mkdirs()
        }
        return File.createTempFile(picture, s, tempDir)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    assert(result != null)
                    val file = File(
                        Objects.requireNonNull(
                            getPath(
                                this@ScanActivity,
                                result!!.uri
                            )
                        )
                    )
                    viewModel!!.setBase(convertImageFileToBase64(file),appid!!,appkey!!)
                    /*startActivity(new Intent(ScanActivity.this, ResultsActivity.class)
                            .putExtra("imagePath", getRealPathFromURIPath(result.getUri(), this)));*/
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result!!.error, Toast.LENGTH_LONG)
                        .show()
                }
            }
            else -> {
            }
        }
    }

    private fun getRealPathFromURIPath(
        contentURI: Uri,
        activity: Context
    ): String? {
        val cursor =
            activity.contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) {
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }

    /**
     * Runtime permission
     *
     * @param permission
     * @param requestCode
     */
    private fun askForPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this@ScanActivity,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ScanActivity,
                arrayOf(permission),
                requestCode
            )
        } else {
            if (ContextCompat.checkSelfPermission(
                    this@ScanActivity,
                    Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                if (ContextCompat.checkSelfPermission(
                        this@ScanActivity,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    if (ContextCompat.checkSelfPermission(
                            this@ScanActivity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        startCameraSource()
                    } else {
                        askForPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            WRITE_CAMERA_PERMISSION_CODE
                        )
                    }
                } else {
                    askForPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        READ_EXTERNAL_PERMISSION_CODE
                    )
                }
            } else {
                askForPermission(
                    Manifest.permission.CAMERA,
                    CAMERA_PERMISSION_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(
                this,
                permissions[0]
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            when (requestCode) {
                CAMERA_PERMISSION_CODE -> askForPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    READ_EXTERNAL_PERMISSION_CODE
                )
                READ_EXTERNAL_PERMISSION_CODE -> askForPermission(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    WRITE_CAMERA_PERMISSION_CODE
                )
                WRITE_CAMERA_PERMISSION_CODE -> startCameraSource()
                else -> {
                }
            }
        }
    }

    companion object {
        private val TAG = ScanActivity::class.java.simpleName
        private const val requestPermissionID = 100
        private const val CAMERA_PERMISSION_CODE = 200
        private const val READ_EXTERNAL_PERMISSION_CODE = 300
        private const val WRITE_CAMERA_PERMISSION_CODE = 400
        fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
            val win = activity.window
            val winParams = win.attributes
            if (on) {
                winParams.flags = winParams.flags or bits
            } else {
                winParams.flags = winParams.flags and bits.inv()
            }
            win.attributes = winParams
        }
    }
}