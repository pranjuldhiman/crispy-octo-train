package com.rs.roundupclasses.subjectlist

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.roundupclasses.apprepository.RoundUpRepository
import com.rs.roundupclasses.apprepository.RoundUpRepositoryImpl
import com.rs.roundupclasses.dashboard.model.MODSubjectModelResponse
import com.rs.roundupclasses.dashboard.model.Subcategory
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class SubjectViewModel (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<List<Subcategory>?>()
    val listOfSearchResult: LiveData<List<Subcategory>?> = _listOfSearchResult
    private val _status = MutableLiveData<ApiStatus>()
    val status : LiveData<ApiStatus?>
        get() = _status
    fun getSubjectData(userid:String,id:String) {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getSubject(userid,id)){
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
            Log.d("SUBJECTDATA", "Api response is......"+Gson().toJson(data))
            if (data.status == 200){
                _listOfSearchResult.value = data.data
            }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
        }
    }

}