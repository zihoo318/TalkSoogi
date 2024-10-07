// CallerPredictionViewModel.kt
package com.example.talkssogi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class CallerPredictionViewModel : ViewModel() {
    // 테스트 중 원인 분석을 위한 로그 보기 설정 (OkHttpClient 설정)
    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("Content-Type", "text/plain; charset=utf-8")
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    var gson = GsonBuilder().setLenient().create()
    private val apiService = Retrofit.Builder() //api 사용을 위한 객체
        .baseUrl(Constants.BASE_URL)
        .client(client) // OkHttpClient를 Retrofit에 설정 (원인 분석을 위한 로그를 보기위한 설정)
        .addConverterFactory(ScalarsConverterFactory.create()) // String 변환기
        .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 변환
        .build()
        .create(ApiService::class.java)

    private val _callerPrediction = MutableLiveData<String>()
    val callerPrediction: LiveData<String> get() = _callerPrediction

    private val _basicActivityAnalysis = MutableLiveData<List<String>>() // 새로운 LiveData 추가
    val basicActivityAnalysis: LiveData<List<String>> get() = _basicActivityAnalysis


    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // 실제 예측 결과 가져오기
    fun fetchCallerPrediction(crnum: Int, keyword: String) {
        viewModelScope.launch {
            try {
                val response = apiService.getCallerPrediction(crnum, keyword)
                response.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            _callerPrediction.value = response.body()
                        } else {
                            _error.value = "Error: ${response.message()}"
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _error.value = "Failed to fetch data: ${t.message}"
                    }
                })
            } catch (e: Exception) {
                Log.e("CallerPredictionViewModel", "Error fetching caller prediction", e)
                _error.value = "Unexpected error: ${e.message}"
            }
        }
    }

    // db에 매핑 결과 미리 저장해두기 위한 api
    fun fetchCallerPredictionExante(crnum: Int, existingBasicActivityAnalysis: List<String>) {
        viewModelScope.launch {
            try {
                val response = apiService.getCallerPredictionExante(crnum)
                response.enqueue(object : Callback<String> {
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        if (response.isSuccessful) {
                            Log.d("Page9", "Response body: ${response.body()}")

                            // 서버에서 응답받은 데이터를 가져옴
                            val newCallerPredictionData = response.body()

                            // 기존 데이터를 유지한 채 새로운 데이터를 추가
                            val updatedBasicActivityAnalysis =
                                existingBasicActivityAnalysis.toMutableList()
                            updatedBasicActivityAnalysis.add(newCallerPredictionData ?: "")

                            // 새로 업데이트된 데이터를 뷰모델에 저장
                            _basicActivityAnalysis.value = updatedBasicActivityAnalysis
                        } else {
                            Log.e("Page9", "Error: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.e("Page9", "Failed to fetch data: ${t.message}")
                    }
                })
            } catch (e: Exception) {
                Log.e("Page9", "Error fetching caller prediction exante", e)
            }
        }
    }
}
