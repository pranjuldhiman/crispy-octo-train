package com.android.roundup.apprepository

import com.android.roundup.dashboard.model.MODDashBoardResponse
import com.android.roundup.dashboard.model.MODSubjectModelResponse
import com.android.roundup.dashboard.model.MODVideoModeResponse
import com.android.roundup.models.MODSearchResponse
import com.android.roundup.models.NotificationResponse
import com.android.roundup.networkhelper.ServiceResult
import com.android.roundup.scan.model.MODSafetyModel
import com.android.roundup.models.ResponseModel
import com.google.gson.JsonObject

interface RoundUpRepository {
    suspend fun getSearchResults(searchTag: String): ServiceResult<MODSearchResponse?>
    suspend fun getTokenAsync(s: String, s1: String, s2: String, jsonObject1: JsonObject): ServiceResult<ResponseModel>
    suspend fun isSafeCall(): ServiceResult<MODSafetyModel?>
    suspend fun getDashboardData(userid:String): ServiceResult<MODDashBoardResponse?>
    suspend fun getSubject(userid:String,id:String): ServiceResult<MODSubjectModelResponse?>
    suspend fun getVideos(userid:String,topicid:String): ServiceResult<MODVideoModeResponse?>
    suspend fun getTopic(userid:String,topicid:String): ServiceResult<MODSubjectModelResponse?>
    suspend fun getLogin(mobileno:String): ServiceResult<MODSafetyModel?>
    suspend fun getNotification(userid:String): ServiceResult<NotificationResponse?>
    suspend fun getEditProfile(name:String,email:String,mobileno:String,userid:String): ServiceResult<NotificationResponse?>
}
