package com.android.roundup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.roundup.resultsactivity.ResultsActivity;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class ScanActivity extends AppCompatActivity {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private RelativeLayout mRlSearchView, action_bar;
    private FrameLayout mRlCameraView;
    private TextView mTextView, btn_done;
    private SurfaceView mCameraView;
    private ImageView mCamera, img_back;
    private Button mBackBtn, mCaptureBtn, mSubmitBtn;
    private EditText mSearchText;
    private static final int requestPermissionID = 100;
    private static final int CAMERA_PERMISSION_CODE = 200;
    private static final int READ_EXTERNAL_PERMISSION_CODE = 300;
    private static final int WRITE_CAMERA_PERMISSION_CODE = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
        setContentView(R.layout.search_activity);
        mRlCameraView = findViewById(R.id.rlCameraView);
        mRlSearchView = findViewById(R.id.rlSearchView);
        mCamera = findViewById(R.id.cam);
        mTextView = findViewById(R.id.text_view);
        mCameraView = findViewById(R.id.surfaceView);
        mBackBtn = findViewById(R.id.back_button);
        mCaptureBtn = findViewById(R.id.capture_button);
        mSubmitBtn = findViewById(R.id.submit_button);
        btn_done = findViewById(R.id.btn_done);
        mSearchText = findViewById(R.id.search_text);
        action_bar = findViewById(R.id.action_bar);
        img_back = findViewById(R.id.img_back);

        mCamera.setOnClickListener(v -> {
            askForPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        });
        img_back.setOnClickListener(v -> {
            mRlSearchView.setVisibility(View.VISIBLE);
            mRlCameraView.setVisibility(View.GONE);
            action_bar.setVisibility(View.GONE);
        });
        mCaptureBtn.setOnClickListener(v -> {
            Intent i = new Intent(ScanActivity.this, ResultsActivity.class);
            i.putExtra("SearchTag", mTextView.getText().toString());
            startActivity(i);
        });
        mSubmitBtn.setOnClickListener(v -> {
            Intent i = new Intent(ScanActivity.this, ResultsActivity.class);
            i.putExtra("SearchTag", mSearchText.getText().toString());
            startActivity(i);
        });

        btn_done.setOnClickListener(view -> {
            Intent i = new Intent(ScanActivity.this, ResultsActivity.class);
            i.putExtra("SearchTag", mTextView.getText().toString());
            startActivity(i);
        });

    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void startCameraSource() {
        CropImage.activity(null).setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    private File createTempFile(String picture, String s) throws Exception {
        File tempDir = new File(getFilesDir(), "images");
        //tempDir = new File(tempDir.getAbsolutePath() + "/RoundUp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(picture, s, tempDir);
    }

    private File createTemporaryFile(String picture, String s) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/RoundUp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(picture, s, tempDir);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE:
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    startActivity(new Intent(ScanActivity.this, ResultsActivity.class)
                            .putExtra("imagePath", getRealPathFromURIPath(result.getUri(), this)));
                    Toast.makeText(this, "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;

        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "abc", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURIPath(Uri contentURI, Context activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    /**
     * Runtime permission
     *
     * @param permission
     * @param requestCode
     */
    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(ScanActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanActivity.this, new String[]{permission}, requestCode);
        } else {
            if ((ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED)) {
                if ((ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)) {
                    if ((ContextCompat.checkSelfPermission(ScanActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED)) {
                        startCameraSource();
                    } else {
                        askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_CAMERA_PERMISSION_CODE);
                    }
                } else {
                    askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_PERMISSION_CODE);
                }
            } else {
                askForPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED) {
            switch (requestCode) {
                case CAMERA_PERMISSION_CODE:
                    askForPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_EXTERNAL_PERMISSION_CODE);
                    break;
                case READ_EXTERNAL_PERMISSION_CODE:
                    askForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_CAMERA_PERMISSION_CODE);
                    break;
                case WRITE_CAMERA_PERMISSION_CODE:
                    startCameraSource();
                    break;
                default:
                    break;
            }
        }
    }
}
