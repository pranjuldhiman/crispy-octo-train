package com.rs.roundupclasses.dashboard

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.roundupclasses.apprepository.RoundUpRepository
import com.rs.roundupclasses.apprepository.RoundUpRepositoryImpl
import com.rs.roundupclasses.dashboard.model.Data
import com.rs.roundupclasses.dashboard.model.MODDashBoardResponse
import com.rs.roundupclasses.models.Banners
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainViewModel(private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<List<Data>?>()
    val listOfSearchResult: LiveData<List<Data>?> = _listOfSearchResult

    private val _bannerResult = MutableLiveData<List<Banners>?>()
    val bannerResult: LiveData<List<Banners>?> = _bannerResult

    private val _appid = MutableLiveData<String?>()
    val appid: LiveData<String?> = _appid

    private val _appkey = MutableLiveData<String?>()
    val appkey: LiveData<String?> = _appkey

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    fun getDashboardData(userid:String) {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getDashboardData(userid)){
                is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {
        _status.value = ApiStatus.ERROR

        _serviceException.value = exception
    }

    private fun onSuccessResponse(data: MODDashBoardResponse?) {
        data?.let {
            _status.value = ApiStatus.ERROR
            Log.d("Response", Gson().toJson(data))
            if (data.status == 200){

                _listOfSearchResult.value = data.data
                _bannerResult.value = data.banners
                _appid.value = data.app_id
                _appkey.value = data.app_key

            }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
        }
    }

}
