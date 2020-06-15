package com.android.roundup.service

import com.android.roundup.models.MODSearchResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RoundUpRepositoryService {
    @FormUrlEncoded
    @POST("search.html")
    suspend fun getSearchResults(@Field("terms") searchTag: String): Response<MODSearchResponse?>
}
