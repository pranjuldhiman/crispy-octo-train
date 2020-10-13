package com.android.roundup.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.roundup.apprepository.RoundUpRepository
import com.android.roundup.apprepository.RoundUpRepositoryImpl
import com.android.roundup.dashboard.model.Data
import com.android.roundup.dashboard.model.MODDashBoardResponse
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.scan.model.MODSafetyModel
import com.android.roundup.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginViewModel (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<String?>()
    val listOfSearchResult: LiveData<String?> = _listOfSearchResult

    private val _name = MutableLiveData<String?>()
    val name: LiveData<String?> = _name

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> = _email

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    fun getLogin(mobileno:String) {
        _status.value = ApiStatus.LOADING

        viewModelScope.launch {
            Log.e("Response","response mobno...."+mobileno.toString())

            when(val serviceResult =
                roundUpRepository.getLogin(mobileno.toString())){
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

    private fun onSuccessResponse(data: MODSafetyModel?) {
        data?.let {
            _status.value = ApiStatus.ERROR
            Log.e("Response", Gson().toJson(data))
            if (data.status == 200){
                _name.value = data.name.toString()
                _email.value = data.email.toString()
                _listOfSearchResult.value = data.userid.toString()
            }else{
                _serviceException.value = "We could not found any search result for the given query!"
            }
        }
    }

}