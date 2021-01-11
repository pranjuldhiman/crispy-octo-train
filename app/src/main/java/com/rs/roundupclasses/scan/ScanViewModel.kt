package com.rs.roundupclasses.scan

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rs.roundupclasses.apprepository.RoundUpRepository
import com.rs.roundupclasses.apprepository.RoundUpRepositoryImpl
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.scan.model.MODSafetyModel
import com.rs.roundupclasses.scan.model.ResponseModel
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

    fun setBase(file:String,appid:String,appkey:String) {
        baseFile = "data:image/jpeg;base64,$file"
        getTokenApi(appid,appkey)
    }

    private fun getTokenApi(appid:String,appkey:String) {
        Log.e("RESULTDATA","result base64 array is......."+baseFile);
        val ocrarray = JsonArray()
        ocrarray.add(JsonPrimitive("math"))
        ocrarray.add(JsonPrimitive("text"))
        var booleandata:Boolean = true
        val array = JsonArray()
        array.add(JsonPrimitive("text"))
        array.add(JsonPrimitive("latex_simplified"))
        array.add(JsonPrimitive("wolfram"))

        val jsonObject1 = JsonObject()
        //jsonObject1.addProperty("src", "https://mathpix.com/examples/limit.jpg")
       jsonObject1.addProperty("src", baseFile)
        jsonObject1.add("ocr", ocrarray)
        jsonObject1.addProperty("skip_recrop",true!!)
        jsonObject1.add("formats", array)

        Log.e("MATHPIXAPI","mapthpix is called......"+jsonObject1.toString())

        viewModelScope.launch {
            when(val serviceResult =
         //       roundUpRepository.getTokenAsync("application/json","raghav_professional_gmail_com_8f66ce","8b1ec75e38ed98a95cc5",jsonObject1)){
                roundUpRepository.getTokenAsync("application/json",appid,appkey,jsonObject1)){
                is ServiceResult.Success -> onSuccessApiToken(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(exception: String) {
        _serviceException.value = exception
    }

    private fun onSuccessApiToken(data: ResponseModel) {
        Log.e("MATHPIXAPI","mapthpix is called......"+data.toString())
        Log.e("RESULTDATA","result data is text called ....."+data.wolfram)
        Log.e("RESULTDATA","result data is request_id called ....."+data.request_id)
        Log.e("RESULTDATA","result data is latex called ....."+data.latex)
        Log.e("RESULTDATA","result data is error called ....."+data.error)

        if (data.error.isNullOrEmpty()){
            _searchText.value = data.wolfram
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
        data?.let { _isSafe.value = data.isPaid }
    }
}
