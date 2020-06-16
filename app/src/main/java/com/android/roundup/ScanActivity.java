package com.android.roundup;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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

    private void startCameraSource() {
        /*
        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Log.d(TAG, "Detector dependencies not loaded yet");
        } else {
            //Initialize camerasource to use high resolution and set Autofocus on.
            final CameraSource mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();
            *//**
         * Add call back to SurfaceView and check if camera permission is granted.
         * If permission is granted we can start our cameraSource and pass it to surfaceView
         *//*
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(ScanActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                *//**
         * Release resources for cameraSource
         *//*
                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }

                *//**
         * Detect all the text from camera using TextBlock and the values into a stringBuilder
         * which will then be set to the textView.
         * *//*
                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");
                                }
                        TextBlock item = items.valueAt(0);
                        stringBuilder.append(item.getValue());
                        mTextView.setText(stringBuilder.toString());
                    }
                }
            });
        }
        */
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
                    UCrop.of(mImageUri, Uri.fromFile(new File(ScanActivity.this.getCacheDir(), "CropImage.jpg")))
                            .withAspectRatio(1, 1)
                            .withMaxResultSize(1000, 1000)
                            .start(this, UCrop.REQUEST_CROP);

                }
                break;
            case UCrop.REQUEST_CROP:
                final Uri mImageUri = UCrop.getOutput(data);
                String imagePath = getRealPathFromURIPath(mImageUri, this);
                startActivity(new Intent(ScanActivity.this, PreviewActivity.class).putExtra("imagePath", imagePath));
                break;
        }
    }


    public Bitmap getThumbnail(Uri uri) throws FileNotFoundException, IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();

        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }

        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ? onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        double ratio = (originalSize > THUMBNAIL_SIZE) ? (originalSize / THUMBNAIL_SIZE) : 1.0;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true; //optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) return 1;
        else return k;
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
