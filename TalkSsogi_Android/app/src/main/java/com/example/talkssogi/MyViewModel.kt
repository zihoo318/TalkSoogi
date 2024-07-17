package com.example.talkssogi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

data class UserIdResponse(val userIds: List<String>) // 전체 유저 아이디들
class MyViewModel(application: Application) : AndroidViewModel(application) {
    private val BASE_URL = "https://example.com" // 실제 API 호스트 URL로 대체해야 됨

    private val apiService = Retrofit.Builder() //api 사용을 위한 객체
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private val _userIds = MutableLiveData<List<String>>() // 전체 유저 아이디 목록
    val userIds: LiveData<List<String>>
        get() = _userIds

    private val _chatRoomList = MutableLiveData<List<ChatRoom>>() // 한 유저의 채팅방 목록
    val chatRoomList: LiveData<List<ChatRoom>>
        get() = _chatRoomList

    init {
        fetchUserIds() // 전체 유저 아이디 목록
        fetchChatRooms() // 한 유저의 채팅방 목록
    }

    fun fetchUserIds() { // 서버에 요청하고 전체 유저 목록 받기
        apiService.getAllUserIds().enqueue(object : Callback<UserIdResponse> {
            override fun onResponse(call: Call<UserIdResponse>, response: Response<UserIdResponse>) {
                if (response.isSuccessful) {
                    val userIdResponse = response.body()
                    userIdResponse?.let { _userIds.value = it.userIds }
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<UserIdResponse>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }

    fun getUserIdsLiveData(): LiveData<List<String>> { // 전체 유저 아이디 목록 getter
        return _userIds
    }

    fun addUserId(newID: String) {
        val currentList = _userIds.value?.toMutableList() ?: mutableListOf()
        currentList.add(newID)
        _userIds.value = currentList
    }

    fun fetchChatRooms() { // 서버에 요청하고 한 유저의 채팅방 목록 받기
        // API 요청하여 채팅방 목록 가져오기
        apiService.getChatRooms("Bearer ${getUserToken()}").enqueue(object : Callback<List<ChatRoom>> {
            override fun onResponse(call: Call<List<ChatRoom>>, response: Response<List<ChatRoom>>) {
                if (response.isSuccessful) {
                    val chatRooms = response.body()
                    chatRooms?.let { _chatRoomList.value = it }
                } else {
                    // 오류 처리
                }
            }

            override fun onFailure(call: Call<List<ChatRoom>>, t: Throwable) {
                // 네트워크 오류 처리
            }
        })
    }

    private fun getUserToken(): String {
        val sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences("Session_ID", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userToken", "") ?: ""
    }
}