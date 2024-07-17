package com.example.talkssogi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/userIds") //페이지1에서 쓸 유저 아이디 목록
    fun getAllUserIds(): Call<UserIdResponse>

    @GET("/api/chatrooms") //채팅방 목록
    fun getChatRooms(
        @Query("ID") userID: String?
    ): Call<List<ChatRoom>>

}
