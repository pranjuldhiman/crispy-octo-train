package com.android.roundup.resultsactivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.roundup.apprepository.RoundUpRepository
import com.android.roundup.apprepository.RoundUpRepositoryImpl
import com.android.roundup.models.Data
import com.android.roundup.models.MODSearchResponse
import com.android.roundup.networkhelper.ServiceResult
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ResultsViewModel(private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<List<Data>?>()
    val listOfSearchResult: LiveData<List<Data>?> = _listOfSearchResult

    fun getResultsBySearchTag(searchTag: String) {
        if (searchTag.isNotBlank()){
            viewModelScope.launch {
                when(val serviceResult =
                    roundUpRepository.getSearchResults(searchTag)){
                    is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                    is ServiceResult.Error -> onFailure(serviceResult.exception)
                }
            }
        }else{
            _serviceException.value = "Please search with at least one query!!"
        }
    }

    private fun onFailure(action: String) {
        _serviceException.value = action
    }

    private fun onSuccessResponse(data: MODSearchResponse?) {
        data?.let {
            Log.d("Response", Gson().toJson(data))
            if (data.success == 200){
                _listOfSearchResult.value = data.data
            }else{
                _serviceException.value = "We could not found any search result for the given query!"
            }
        }
    }
}
