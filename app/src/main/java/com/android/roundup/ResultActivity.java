package com.android.roundup;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

public class ResultActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_activity);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d("ResultActivity", "onCreate: ="+bundle.getString("SearchTag"));
        }

    }
}
