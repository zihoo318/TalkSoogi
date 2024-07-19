package com.example.talkssogi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    //로컬 서버에서 테스트 중
    private const val BASE_URL = "http://localhost:8080"

    val api: RankingApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RankingApi::class.java)
    }
}