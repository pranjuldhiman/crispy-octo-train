package com.android.roundup.service

import com.android.roundup.networkhelper.OkHttpClient
import com.android.roundup.networkhelper.RetrofitServiceFactory
import java.util.concurrent.TimeUnit

internal class RoundUpRepositoryRetrofit {
    private val RIGS_IT_BASE_URL: String = "http://trainingscholar.com/studyapp/apis/get/"

    fun prepareService(): RoundUpRepositoryService {
        val retrofitService: RoundUpRepositoryService by lazy {
            RetrofitServiceFactory.getProvider(
                baseUrl = RIGS_IT_BASE_URL,
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
