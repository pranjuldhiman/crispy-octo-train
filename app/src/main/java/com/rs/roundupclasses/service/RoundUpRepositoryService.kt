package com.rs.roundupclasses.service

import com.rs.roundupclasses.dashboard.model.MODDashBoardResponse
import com.rs.roundupclasses.dashboard.model.MODSubjectModelResponse
import com.rs.roundupclasses.dashboard.model.MODVideoModeResponse
import com.rs.roundupclasses.models.MODSearchResponse
import com.rs.roundupclasses.models.NotificationResponse
import com.rs.roundupclasses.scan.model.MODSafetyModel
import com.rs.roundupclasses.scan.model.ResponseModel
import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.*

interface RoundUpRepositoryService {
    @FormUrlEncoded
    @POST("get/search.html")
    suspend fun getSearchResults(@Field("terms") searchTag: String): Response<MODSearchResponse?>

    @POST("latex")
    @Headers("Accept:application/vnd.yourapi.v1.full+json")
    suspend fun getTokenAsync(
        @Header("content-type") applicationtype: String,
        @Header("app_id") app_id: String,
        @Header("app_key") app_key: String,
        @Body body: JsonObject
    ): Response<ResponseModel>

    @GET("is_paid.php")
    suspend fun isSafeCall(): Response<MODSafetyModel>

    @FormUrlEncoded
    @POST("get/dashboard.html")
    suspend fun getDashboardResponse(@Field("user_id") userId: Int): Response<MODDashBoardResponse>

    @FormUrlEncoded
    @POST("get/getsubjects.html")
    suspend fun getSubjectResponse(@Field("user_id") userId: Int,@Field("subcategory_id") subcategory_id: String): Response<MODSubjectModelResponse>

    @FormUrlEncoded
    @POST("get/getvideos.html")
    suspend fun getVideosResponse(@Field("user_id") userId: Int,@Field("topic_id") subcategory_id: String): Response<MODVideoModeResponse>

    @FormUrlEncoded
    @POST("get/getvideostest.html")
    suspend fun getVideosResponseTest(@Field("user_id") userId: Int,@Field("topic_id") subcategory_id: String): Response<MODVideoModeResponse>


    @FormUrlEncoded
    @POST("get/gettopics.html")
    suspend fun getTopicResponse(@Field("user_id") userId: Int,@Field("subject_id") subcategory_id: String): Response<MODSubjectModelResponse>

    @FormUrlEncoded
    @POST("user/check.html")
    suspend fun getLogin(@Field("mobile") userId: String,@Field("fcm_token") token: String): Response<MODSafetyModel>

    @FormUrlEncoded
    @POST("user/updatefcm.html")
    suspend fun updateFcm(@Field("userid") userId: String,@Field("fcm_token") token: String): Response<MODSafetyModel>

    @FormUrlEncoded
    @POST("get/notifications.html")
    suspend fun getNotification(@Field("userid") userId: String): Response<NotificationResponse>

    @FormUrlEncoded
    @POST("user/editprofile.html")
    suspend fun getProfileNotification(@Field("name") name: String,@Field("email") email: String, @Field("mobile") mobile: String, @Field("userid") userid: String): Response<NotificationResponse>

}
