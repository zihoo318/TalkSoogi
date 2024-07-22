package com.example.talkssogi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActivityAnalysisViewModel : ViewModel() {
    private val BASE_URL = "http://10.0.2.2:8080/" // 실제 API 호스트 URL로 대체해야 됨 //에뮬레이터에서 호스트 컴퓨터의 localhost를 가리킴

    private val apiService = Retrofit.Builder() //api 사용을 위한 객체
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private val _imageUrls = MutableLiveData<List<ImageURL>>() //페이지9 이미지url
    val imageUrls: LiveData<List<ImageURL>> get() = _imageUrls

    private val _participants = MutableLiveData<List<String>>() // 페이지9 대화 참가자 이름 리스트
    val participants: LiveData<List<String>> get() = _participants



    fun getActivityAnalysisImage(searchData: Page9SearchData) { //페이지9에서 쓸 검색 정보 보내고 이미지 주소 받기
        apiService.getActivityAnalysisImage(searchData).enqueue(object : Callback<List<ImageURL>> {
            override fun onResponse(call: Call<List<ImageURL>>, response: Response<List<ImageURL>>) {
                if (response.isSuccessful) {
                    val ImageURL = response.body()
                    ImageURL?.let { _imageUrls.value = it }
                } else {
                    println("Failed to fetch images: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<ImageURL>>, t: Throwable) {
                println("Network error: ${t.message}")
            }
        })
    }

    fun loadParticipants(chatRoomId: Int) { // 채팅방 대화 참여자 이름 가져와서 리스트로 변환 (페이지9 + 윤지 파트? api 같은거 가져다 씀)
        viewModelScope.launch {
            try {
                val response = apiService.getChattingRoomMembers(chatRoomId).execute()
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
}