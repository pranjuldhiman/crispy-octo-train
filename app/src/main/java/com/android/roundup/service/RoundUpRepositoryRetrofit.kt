package com.android.roundup.service

import com.android.roundup.networkhelper.OkHttpClient
import com.android.roundup.networkhelper.RetrofitServiceFactory
import java.util.concurrent.TimeUnit

internal class RoundUpRepositoryRetrofit {
    private val ROUND_UP_BASE_URL: String = "http://trainingscholar.com/studyapp/apis/"
    private val BASE_URL = "https://api.mathpix.com/v3/"
    private val SAFETY_URL: String = "http://vchbillings.info/"

    companion object {
        const val THUMBNAIL_IMAGE_URL = "https://trainingscholar.com/studyapp/assets/images/thumbnails/"
    }

    fun prepareService(): RoundUpRepositoryService {
        val retrofitService: RoundUpRepositoryService by lazy {
            RetrofitServiceFactory.getProvider(
                baseUrl = ROUND_UP_BASE_URL,
                serviceClass = RoundUpRepositoryService::class.java,
                client = OkHttpClient.instance.newBuilder()
                    .connectTimeout(180 , TimeUnit.SECONDS)
                    .readTimeout(180 , TimeUnit.SECONDS)
                    .build()
            )
        }
        return retrofitService
    }

    fun prepareMathPixService(): RoundUpRepositoryService {
        val retrofitService: RoundUpRepositoryService by lazy {
            RetrofitServiceFactory.getProvider(
                baseUrl = BASE_URL,
                serviceClass = RoundUpRepositoryService::class.java,
                client = OkHttpClient.instance.newBuilder()
                    .connectTimeout(180 , TimeUnit.SECONDS)
                    .readTimeout(180 , TimeUnit.SECONDS)
                    .build()
            )
        }
        return retrofitService
    }

    fun prepareSafetyService(): RoundUpRepositoryService {
        val retrofitService: RoundUpRepositoryService by lazy {
            RetrofitServiceFactory.getProvider(
                baseUrl = SAFETY_URL,
                serviceClass = RoundUpRepositoryService::class.java,
                client = OkHttpClient.instance.newBuilder()
                    .connectTimeout(180 , TimeUnit.SECONDS)
                    .readTimeout(180 , TimeUnit.SECONDS)
                    .build()
            )
        }
        return retrofitService
    }
}
