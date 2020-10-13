package com.android.roundup.dashboard

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
import com.android.roundup.models.Banners
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.utils.ApiStatus
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
            }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
        }
    }

}
