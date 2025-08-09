package com.aspa.aspa.network

import com.aspa.aspa.data.remote.service.QuestionApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitProvider {
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://asia-northeast3-aspa-7126b.cloudfunctions.net")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    fun <T> create(service: Class<T>): T = retrofit.create(service)
}