package com.rs.roundupclasses.profile

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.roundupclasses.apprepository.RoundUpRepository
import com.rs.roundupclasses.apprepository.RoundUpRepositoryImpl
import com.rs.roundupclasses.models.NotificationResponse
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ProfileViewModel (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<String?>()
    val listOfSearchResult: LiveData<String?> = _listOfSearchResult

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    private val _message = MutableLiveData<String?>()
    val message: LiveData<String?> = _message

    fun editProfile(name:String,email:String,mobileno:String,userid:String) {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            Log.e("Response","response mobno...."+mobileno.toString())
            when(val serviceResult =
                roundUpRepository.getEditProfile(name.toString(),email.toString(),mobileno.toString(),userid.toString())){
                is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {

        Log.e("Response", "exception is....."+exception.toString())

        _status.value = ApiStatus.ERROR

        _serviceException.value = exception
    }

    private fun onSuccessResponse(data: NotificationResponse?) {
        data?.let {
            _status.value = ApiStatus.ERROR
            Log.e("EDITPROFILE", Gson().toJson(data))
            if (data.status == 200){
                _message.value ="Profile is update"
            }else{
                _serviceException.value = "Something went wrong!"
            }
        }
    }

}