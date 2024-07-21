package com.example.talkssogi


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface RankingApi {
    @GET("/basicRankingResults")
    suspend fun getBasicRankingResults(): Map<String, List<String>>

    @GET("/searchRankingResults")
    suspend fun getSearchRankingResults(): Map<String, List<String>>
}
