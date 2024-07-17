package com.example.talkssogi

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {

    @GET("/api/userIds") //페이지1에서 쓸 유저 아이디 목록
    fun getAllUserIds(): Call<UserIdResponse>
    @GET("/api/chatrooms") //채팅방 목록
    fun getChatRooms(
        @Header("Authorization") authToken: String  //Authorization 헤더에 authToken이라는 이름으로 세션 ID 전달
    ): Call<List<ChatRoom>>

}
