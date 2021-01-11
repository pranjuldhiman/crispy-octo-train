package com.rs.notify

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.roundupclasses.apprepository.RoundUpRepository
import com.rs.roundupclasses.apprepository.RoundUpRepositoryImpl
import com.rs.roundupclasses.models.Notification
import com.rs.roundupclasses.models.NotificationResponse
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class NotificationViewModel (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfNotificationResult = MutableLiveData<List<Notification>?>()
    val listOfNotificationResult: LiveData<List<Notification>?> = _listOfNotificationResult



    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    fun getNotificationData(userid:String) {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getNotification(userid)){
                is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {
        _status.value = ApiStatus.ERROR

        _serviceException.value = exception
    }

    private fun onSuccessResponse(data: NotificationResponse?) {
        data?.let {
            _status.value = ApiStatus.ERROR
            Log.d("Response", Gson().toJson(data))
            if (data.status == 200){

                _listOfNotificationResult.value = data.list
            }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
        }
    }

}