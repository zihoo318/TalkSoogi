package com.example.talkssogi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class ActivityAnalysisViewModel : ViewModel() {
    //private val BASE_URL = "http://10.0.2.2:8080/" // 실제 API 호스트 URL로 대체해야 됨 //에뮬레이터에서 호스트 컴퓨터의 localhost를 가리킴
    private val BASE_URL = "http://192.168.45.208:8080/"  // 실제 안드로이드 기기에서 실행 할 때

    // 테스트 중 원인 분석을 위한 로그 보기 설정 (OkHttpClient 설정)
    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()


    private val apiService = Retrofit.Builder() //api 사용을 위한 객체
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // JSON 변환
        .addConverterFactory(ScalarsConverterFactory.create()) // 문자열 변환
        .build()
        .create(ApiService::class.java)

    private val _imageUrls = MutableLiveData<List<ImageURL>>() //페이지9 이미지url
    val imageUrls: LiveData<List<ImageURL>> get() = _imageUrls

    private val _participants = MutableLiveData<List<String>>() // 페이지9 대화 참가자 이름 리스트
    val participants: LiveData<List<String>> get() = _participants

    // 페이지6 워드 클라우드 이미지 URL, LiveData 변수를 초기화
    private val _wordCloudImageUrl = MutableLiveData<String?>() // Nullable로 설정
    val wordCloudImageUrl: LiveData<String?> get() = _wordCloudImageUrl

    //페이지9에서 쓸 검색 정보 보내고 이미지 주소 받기
    fun getActivityAnalysisImage(startDate: String?, endDate: String?, searchWho: String, resultsItem: String, crnum: Int) {
        // 모든 필드가 null이 아니어야 API 호출을 진행합니다
        if (startDate != null && endDate != null && searchWho.isNotEmpty() && resultsItem.isNotEmpty()) {
            apiService.getActivityAnalysisImage(startDate, endDate, searchWho, resultsItem, crnum)
                .enqueue(object : Callback<List<ImageURL>> {
                    override fun onResponse(
                        call: Call<List<ImageURL>>,
                        response: Response<List<ImageURL>>
                    ) {
                        if (response.isSuccessful) {
                            val ImageURL = response.body()
                            ImageURL?.let { _imageUrls.value = it }
                            Log.d("FileUpload", "이미지 생성 및 전달 받기 성공 : ${ImageURL}")
                        } else {
                            Log.d("FileUpload", "이미지 생성 및 전달 받기 실패")
                        }
                    }

                    override fun onFailure(call: Call<List<ImageURL>>, t: Throwable) {
                        Log.d("FileUpload", "이미지 생성 및 전달 받기 실패 네트워크 에러")
                    }
                })
        }
    }

    // 채팅방 대화 참여자 이름 가져와서 리스트로 변환
    fun loadParticipants(crnum: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getChattingRoomMembers(crnum).execute()
                if (response.isSuccessful) {
                    val participantsList = response.body() ?: emptyList()
                    _participants.postValue(participantsList)
                } else {
                    // 에러 처리
                    _participants.postValue(emptyList())
                }
            } catch (e: Exception) {
                // 예외 처리
                _participants.postValue(emptyList())
            }
        }
    }

    // 워드 클라우드 이미지 URL 가져오기
    fun loadWordCloudImageUrl(crnum: Int, userId: Int) {
        viewModelScope.launch {
            try {
                // activityAnalysisViewModel.getWordCloudImageUrl 호출 후, imageUrls가 wordCloudImageUrl로 수정됨
                val response = apiService.getWordCloudImageUrl(crnum, userId).execute()
                if (response.isSuccessful) {
                    val wordCloudUrl = response.body()
                    _wordCloudImageUrl.postValue(wordCloudUrl) // Nullable 값을 설정
                } else {
                    _wordCloudImageUrl.postValue(null) // 실패시 null 설정
                }
            } catch (e: Exception) {
                _wordCloudImageUrl.postValue(null) // 예외 발생시 null 설정
            }
        }
    }
}