package com.rs.roundupclasses.apprepository

import android.util.Log
import com.rs.roundupclasses.dashboard.model.MODDashBoardResponse
import com.rs.roundupclasses.dashboard.model.MODSubjectModelResponse
import com.rs.roundupclasses.dashboard.model.MODVideoModeResponse
import com.rs.roundupclasses.models.MODSearchResponse
import com.rs.roundupclasses.models.NotificationResponse
import com.rs.roundupclasses.networkhelper.RetrofitCallbackHandler
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.scan.model.MODSafetyModel
import com.rs.roundupclasses.scan.model.ResponseModel
import com.rs.roundupclasses.service.RoundUpRepositoryRetrofit
import com.rs.roundupclasses.service.RoundUpRepositoryService
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

        Log.e("MATHPIXAPI","mathpix api is called applicationtype........."+applicationtype)
        Log.e("MATHPIXAPI","mathpix api is called app_id........."+app_id)
        Log.e("MATHPIXAPI","mathpix api is called app_key........."+app_key)
        Log.e("MATHPIXAPI","mathpix api is called body........."+body)

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
                Log.e("GETVIDEOS","getVideos userid is......"+userid.toString())
                if(topicid.toString().equals("ONE"))
                {
                    roundUpService.getVideosResponseTest(userid.toInt(),"1")
                }
                else
                {
                    roundUpService.getVideosResponse(userid.toInt(),topicid.toString())
                }
            }
        }
    }

    override suspend fun getTopic(userid:String,topicid: String): ServiceResult<MODSubjectModelResponse?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("GETTOPIC","getTopic param is......"+topicid.toString())
                Log.e("GETTOPIC","userid param is......"+userid.toString())
                roundUpService.getTopicResponse(userid.toInt(),topicid.toString())
//                roundUpService.getTopicResponse(userid.toInt(),"1")
            }
        }
    }

    override suspend fun getLogin(mobileno: String,token:String): ServiceResult<MODSafetyModel?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("APIRESPONSE","login api is mobile no param is......"+mobileno.toString())
                Log.e("APIRESPONSE","login api is token param is......"+token.toString())
                roundUpService.getLogin(mobileno.toString(),token.toString())
            }
        }
    }

    override suspend fun updateFcm(userID: String,token:String): ServiceResult<MODSafetyModel?> {
        return withContext(ioDispatcher) {
            RetrofitCallbackHandler.processCall {
                Log.e("APIRESPONSE","updateFCM api is mobile no param is......"+userID.toString())
                Log.e("APIRESPONSE","updateFCM api is token param is......"+token.toString())
                roundUpService.updateFcm(userID.toString(),token.toString())
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
