package com.android.roundup.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.roundup.apprepository.RoundUpRepository
import com.android.roundup.apprepository.RoundUpRepositoryImpl
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.scan.model.MODSafetyModel
import com.android.roundup.models.ResponseModel
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.launch

class ScanViewModel: ViewModel() {
    private var baseFile:String=""
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()

    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _searchText = MutableLiveData<String>()
    val searchText: LiveData<String> = _searchText
    private val _isSafe = MutableLiveData<Int>()
    val isSafe: LiveData<Int> = _isSafe

    fun setBase(file:String) {
        baseFile = "data:image/jpeg;base64,$file"
        getTokenApi()
    }

    private fun getTokenApi() {
        val array = JsonArray()
        array.add(JsonPrimitive("text"))
        val jsonObject1 = JsonObject()
        jsonObject1.addProperty("src", baseFile)
        jsonObject1.add("formats", array)

        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getTokenAsync("application/json","raghav_professional_gmail_com_8f66ce","8b1ec75e38ed98a95cc5",jsonObject1)){
                is ServiceResult.Success -> onSuccessApiToken(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {
        _serviceException.value = exception
    }

    private fun onSuccessApiToken(data: ResponseModel) {
        if (data.error.isNullOrEmpty()){
            _searchText.value = data.text
        }else{
            _serviceException.value = data.error
        }
    }

    fun isSafeCall(){
        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.isSafeCall()){
                is ServiceResult.Success -> onSuccessSafeCall(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onSuccessSafeCall(data: MODSafetyModel?) {
        data?.let { //_isSafe.value = data.isPaid
             }
    }
}
