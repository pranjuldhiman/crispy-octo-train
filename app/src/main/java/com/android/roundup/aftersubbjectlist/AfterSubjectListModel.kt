package com.android.roundup.aftersubbjectlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.roundup.apprepository.RoundUpRepository
import com.android.roundup.apprepository.RoundUpRepositoryImpl
import com.android.roundup.dashboard.model.MODSubjectModelResponse
import com.android.roundup.dashboard.model.Subcategory
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AfterSubjectListModel  (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<List<Subcategory>?>()
    val listOfSearchResult: LiveData<List<Subcategory>?> = _listOfSearchResult

    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status

    fun gettopicData(userid:String,topicid:String) {
        _status.value = ApiStatus.LOADING

        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getTopic(userid,topicid)){
                is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {
        _status.value = ApiStatus.ERROR

        _serviceException.value = exception
    }

    private fun onSuccessResponse(data: MODSubjectModelResponse?) {
        _status.value = ApiStatus.ERROR

        data?.let {
            Log.d("SUBJECTDATA", "Api response is......"+ Gson().toJson(data))
            if (data.status == 200){
                _listOfSearchResult.value = data.data
            }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
        }
    }

}