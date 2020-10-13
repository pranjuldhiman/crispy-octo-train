package com.android.roundup.examcontent.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.android.roundup.apprepository.RoundUpRepository
import com.android.roundup.apprepository.RoundUpRepositoryImpl
import com.android.roundup.dashboard.model.MODVideoModeResponse
import com.android.roundup.dashboard.model.Videos
import com.android.roundup.networkhelper.ServiceResult
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PdfFragmentViewModel (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<List<Videos>?>()
    val listOfSearchResult: LiveData<List<Videos>?> = _listOfSearchResult

    fun getVideoData(userid:String,topicid:String) {
        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getVideos(userid,topicid)){
                is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {
        _serviceException.value = exception
    }

    private fun onSuccessResponse(data: MODVideoModeResponse?) {
        data?.let {
            Log.d("GETVIDEOS", "videos is......"+Gson().toJson(data))
            if (data.status == 200){
                _listOfSearchResult.value = data.data
            }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
        }
    }

}