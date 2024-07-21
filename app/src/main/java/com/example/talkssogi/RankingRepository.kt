package com.example.talkssogi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RankingRepository {
    private val apiService: RankingApi

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080") // 실제 서버 주소로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(RankingApi::class.java)
    }

    suspend fun getBasicRankingResults(): Map<String, List<String>> {
        return apiService.getBasicRankingResults()
    }

    suspend fun getSearchRankingResults(): Map<String, List<String>> {
        return apiService.getSearchRankingResults()
    }
}
