package com.android.roundup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.android.roundup.resultsactivity.ResultsActivity;
import com.yalantis.ucrop.UCrop;

import java.io.File;

public class ScanActivity extends AppCompatActivity {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int THUMBNAIL_SIZE = 500;
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
    private boolean isCameraPermission = false;
    private Uri mImageUri = null;

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
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = null;
        try {
            photo = this.createTemporaryFile("picture", ".jpg");
            photo.delete();
        } catch (Exception e) {
        }
        //mImageUri = Uri.fromFile(photo);
        mImageUri = FileProvider.getUriForFile(ScanActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        startActivityForResult(intent, 0);
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
            case 0:
                if (resultCode == Activity.RESULT_OK) {
                    UCrop ucrop = UCrop.of(mImageUri, Uri.fromFile(new File(ScanActivity.this.getCacheDir(), "CropImage.jpg")))
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(1000, 1000);
                    ucrop = advancedConfig(ucrop);
                    ucrop.start(this, UCrop.REQUEST_CROP);

                }
                break;
            case UCrop.REQUEST_CROP:
                final Uri mImageUri = UCrop.getOutput(data);
                String imagePath = getRealPathFromURIPath(mImageUri, this);
                startActivity(new Intent(ScanActivity.this, PreviewActivity.class).putExtra("imagePath", imagePath));
                break;
        }
    }

    private UCrop advancedConfig(UCrop ucrop) {
        UCrop.Options options = new UCrop.Options();
        options.setFreeStyleCropEnabled(true);
        return ucrop.withOptions(options);
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
