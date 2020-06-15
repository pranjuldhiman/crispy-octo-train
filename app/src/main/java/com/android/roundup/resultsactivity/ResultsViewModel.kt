package com.android.roundup.resultsactivity

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.roundup.apprepository.RoundUpRepository
import com.android.roundup.apprepository.RoundUpRepositoryImpl
import com.android.roundup.models.MODSearchResponse
import com.android.roundup.networkhelper.ServiceResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.Gson
import kotlinx.coroutines.launch

class ResultsViewModel(private val context: Context): ViewModel() {
    private val roundUpRepository: RoundUpRepository = RoundUpRepositoryImpl()


    fun getResultsBySearchTag(searchTag: String) {
        viewModelScope.launch {
            when(val serviceResult =
                roundUpRepository.getSearchResults(searchTag)){
                is ServiceResult.Success -> onSuccessResponse(serviceResult.data)
                is ServiceResult.Error -> onFailure(serviceResult.exception)
            }
        }
    }

    private fun onFailure(action: String) {

    }

    private fun onSuccessResponse(data: MODSearchResponse?) {
        data?.let {
            Log.d("Response", Gson().toJson(data))
        }
    }
}
