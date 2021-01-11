package com.rs.roundupclasses.apprepository

import com.rs.roundupclasses.dashboard.model.MODDashBoardResponse
import com.rs.roundupclasses.dashboard.model.MODSubjectModelResponse
import com.rs.roundupclasses.dashboard.model.MODVideoModeResponse
import com.rs.roundupclasses.models.MODSearchResponse
import com.rs.roundupclasses.models.NotificationResponse
import com.rs.roundupclasses.networkhelper.ServiceResult
import com.rs.roundupclasses.scan.model.MODSafetyModel
import com.rs.roundupclasses.scan.model.ResponseModel
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
