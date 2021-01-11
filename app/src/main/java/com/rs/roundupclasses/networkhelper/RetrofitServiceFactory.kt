package com.rs.roundupclasses.networkhelper

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitServiceFactory {
    fun <T> getProvider(
        client: okhttp3.OkHttpClient = OkHttpClient.instance,
        serviceClass: Class<T>,
        baseUrl: String
    ): T {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()

        return retrofit.create(serviceClass)
    }
}
