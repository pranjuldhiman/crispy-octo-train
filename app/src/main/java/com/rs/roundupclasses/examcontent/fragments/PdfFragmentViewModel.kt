package com.rs.roundupclasses.examcontent.fragments

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.rs.roundupclasses.apprepository.RoundUpRepository
import com.rs.roundupclasses.apprepository.RoundUpRepositoryImpl
import com.rs.roundupclasses.dashboard.model.MODVideoModeResponse
import com.rs.roundupclasses.dashboard.model.Videos
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.google.gson.Gson
import kotlinx.coroutines.launch

class PdfFragmentViewModel (private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()
    private val _serviceException = MutableLiveData<String?>()
    val serviceException: LiveData<String?> = _serviceException
    private val _listOfSearchResult = MutableLiveData<List<Videos>?>()
    val listOfSearchResult: LiveData<List<Videos>?> = _listOfSearchResult
    var listOfSearchResults: MutableList<Videos>? =  mutableListOf()

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
            Log.d("GETVIDEOS", "videos is......" + Gson().toJson(data))
            if (data.status == 200) {
                for (position in data.data!!.indices) {
                    if (!data.data!!.get(position).pdfurl.equals(null)) {
                        listOfSearchResults!!.add(
                            Videos(
                                data.data!!.get(position).id,
                                data.data!!.get(position).category_id,
                                data.data!!.get(position).topic_id,
                                data.data!!.get(position).subject_id,
                                data.data!!.get(position).class_id,
                                data.data!!.get(position).title,
                                data.data!!.get(position).description,
                                data.data!!.get(position).thumbnail,
                                data.data!!.get(position).mediatype,
                                data.data!!.get(position).mediaurl,
                                data.data!!.get(position).videourl,
                                data.data!!.get(position).pdfurl,
                                data.data!!.get(position).medialength,
                                data.data!!.get(position).is_active,
                                data.data!!.get(position).created_datetime
                            )
                        )
                    }
                    _listOfSearchResult.value = listOfSearchResults
                }/*else{
                _serviceException.value = "We could not found any search result for the given query!"
            }*/
            }
        }
    }

}