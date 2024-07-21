package com.example.talkssogi

import android.util.Log
import com.example.talkssogi.RankingRepository.apiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RankingRepository {
    private val apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/") // 실제 서버 주소로 변경
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)
    }

    suspend fun getBasicRankingResults(): Map<String, List<String>> {
        return try {
            val response = apiService.getBasicRankingResults()
            response.body() ?: emptyMap()
        } catch (e: Exception) {
            Log.e("RankingRepository", "Error fetching basic ranking results", e)
            emptyMap()
        }
    }

    suspend fun getSearchRankingResults(): Map<String, List<String>> {
        return try {
            val response = apiService.getSearchRankingResults()
            response.body() ?: emptyMap()
        } catch (e: Exception) {
            Log.e("RankingRepository", "Error fetching search ranking results", e)
            emptyMap()
        }
    }
    //페이지 8의 기본 동작(가을 동작)
    suspend fun getActivityAnalysis(): Map<String, List<String>> {
        return try {
            val response = apiService.getActivityAnalysis()
            response.body() ?: emptyMap()
        } catch (e: Exception) {
            Log.e("RankingRepository", "Error fetching search ranking results", e)
            emptyMap()
        }
    }
}



