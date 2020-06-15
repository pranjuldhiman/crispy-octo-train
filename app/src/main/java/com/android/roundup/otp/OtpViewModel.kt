package com.android.roundup.otp

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.roundup.R

class OtpViewModel(private val context: Context): ViewModel() {
    private var _otpError = MutableLiveData<String>()
    var otpError: LiveData<String> = _otpError
    private var _progress = MutableLiveData<Boolean>()
    var progress: LiveData<Boolean> = _progress

    fun validate(enteredOtp: String?) {
        if (TextUtils.isEmpty(enteredOtp)){
            _otpError.value = context.getString(R.string.enter_otp_error)
            _progress.value = false
        }else{
            _progress.value = true
        }
    }
}
