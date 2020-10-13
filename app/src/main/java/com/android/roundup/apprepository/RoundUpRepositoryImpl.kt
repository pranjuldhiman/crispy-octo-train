package com.android.roundup.apprepository

import android.util.Log
import com.android.roundup.dashboard.model.MODDashBoardResponse
import com.android.roundup.dashboard.model.MODSubjectModelResponse
import com.android.roundup.dashboard.model.MODVideoModeResponse
import com.android.roundup.models.MODSearchResponse
import com.android.roundup.models.NotificationResponse
import com.android.roundup.networkhelper.RetrofitCallbackHandler
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.scan.model.MODSafetyModel
import com.android.roundup.models.ResponseModel
import com.android.roundup.service.RoundUpRepositoryRetrofit
import com.android.roundup.service.RoundUpRepositoryService
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoundUpRepositoryImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val roundUpService: RoundUpRepositoryService = RoundUpRepositoryRetrofit().prepareService()
): RoundUpRepository {
    override suspend fun getSearchResults(searchTag: String): ServiceResult<MODSearchResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                roundUpService.getSearchResults(searchTag)
            }
        }
    }

    override suspend fun getTokenAsync(
        applicationtype: String,
        app_id: String,
        app_key: String,
        body: JsonObject
    ): ServiceResult<ResponseModel> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                RoundUpRepositoryRetrofit().prepareMathPixService().getTokenAsync(applicationtype, app_id, app_key, body)
            }
        }
    }

    override suspend fun isSafeCall(): ServiceResult<MODSafetyModel?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                RoundUpRepositoryRetrofit().prepareSafetyService().isSafeCall()
            }
        }
    }

    override suspend fun getDashboardData(userid:String): ServiceResult<MODDashBoardResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                roundUpService.getDashboardResponse(userid.toInt())
            }
        }
    }

    override suspend fun getSubject(userid:String,id:String): ServiceResult<MODSubjectModelResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("SUBJECTDATA","getsubject param is......"+id.toString())
                roundUpService.getSubjectResponse(userid.toInt(),id.toString())
            }
        }
    }

    override suspend fun getVideos(userid:String,topicid: String): ServiceResult<MODVideoModeResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("GETVIDEOS","getVideos param is......"+topicid.toString())
                roundUpService.getVideosResponse(userid.toInt(),topicid.toString())
            }
        }
    }

    override suspend fun getTopic(userid:String,topicid: String): ServiceResult<MODSubjectModelResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("GETTOPIC","getTopic param is......"+topicid.toString())
                Log.e("GETTOPIC","userid param is......"+userid.toString())
               // roundUpService.getTopicResponse(1,topicid.toString())
                roundUpService.getTopicResponse(userid.toInt(),"1")
            }
        }
    }

    override suspend fun getLogin(mobileno: String): ServiceResult<MODSafetyModel?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("GETVIDEOS","getVideos param is......"+mobileno.toString())
                roundUpService.getLogin(mobileno.toString())
            }
        }
    }

    override suspend fun getNotification(userid: String): ServiceResult<NotificationResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("GETVIDEOS","getVideos param is......"+userid.toString())
                roundUpService.getNotification(userid.toString())
            }
        }
    }

    override suspend fun getEditProfile(name: String,email: String,mobileno: String,userid: String): ServiceResult<NotificationResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {

                roundUpService.getProfileNotification(name.toString(),email.toString(),mobileno.toString(),userid.toString())
            }
        }
    }


}
