package com.android.roundup.utils

import com.android.roundup.models.ResponseModel
import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit


enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all")
}

private const val BASE_URL = "http://trainingscholar.com/studyapp/apis/"
private const val BASE_URLS = "https://github.com/Mukeshsingh93/"
//private const val BASE_URL = " http://enetwork.esmartg.com/api/"
//private const val BASE_URL = "http://softgates.ae/recharge/api/"

enum class ApiStatus { LOADING, ERROR, DONE }

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()


private val retrofits = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create(Gson()))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()


/**
 * A public interface that exposes the [getProperties] method
 */

interface RoundupApiService {

    @FormUrlEncoded
    @POST("token")
    @Headers("Accept:application/vnd.yourapi.v1.full+json",
        "loginType: LIS")
    fun getToken(
        @Field("grant_type") grant_type: String,
        @Field("UserName") UserName: String,
        @Field("Password") Password: String
    ): Deferred<ResponseModel>


    @FormUrlEncoded
    @POST("get/dashboard.html")
    fun getDashboard(
        @Field("user_id") user_id: String
    ): Deferred<ResponseModel>


    companion object {
        fun getService(): RoundupApiService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()
            return Retrofit.Builder()
//                .baseUrl("http://18.218.168.60/hotshelf/api/")
                .baseUrl("https://www.hotshelf.com/dev/api/")
                .addConverterFactory(MoshiConverterFactory.create())
                .client(okHttpClient)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build().create(RoundupApiService::class.java)
            // .baseUrl("https://androidwave.com")
        }
    }
}

object RoundupApi {
    val retrofitService: RoundupApiService by lazy { retrofit.create(RoundupApiService::class.java) }
}


object RoundupApis {
    val retrofitServices: RoundupApiService by lazy { retrofits.create(RoundupApiService::class.java) }
}

