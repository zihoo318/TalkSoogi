package com.example.talkssogi

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/api/userIds") //페이지1에서 쓸 유저 아이디 목록
    fun getAllUserIds(): Call<UserIdResponse> // 업로드 성공 여부를 확인하기 위한 응답

    @POST("/api/userId") //페이지1에서 쓸 유저 생성(아이디 입력 후 확인 버튼 누르면)
    fun sendUserId(
        @Body user: User
    ): Call<ResponseBody>

    @GET("/api/chatrooms") //채팅방 목록
    fun getChatRooms(
        @Query("ID") userID: String?
    ): Call<List<ChatRoom>> // 업로드 성공 여부를 확인하기 위한 응답

    @Multipart //파일 전송
    @POST("/api/uploadfile")
    fun uploadFile(
        @Part file: MultipartBody.Part, // 업로드할 파일을 MultipartBody.Part 형식으로 전달
        @Query("userId") userID: String?,
        @Query("headcount") headcount : Int?
    ): Call<ResponseBody>

    @GET("/api/rankings/basicRankingResults")   //페이지7에서 사용할 랭킹 배열
    suspend fun getBasicRankingResults(): Response<Map<String, List<String>>>

    @GET("/api/rankings/searchRankingResults")  //페이지7에서 사용할 랭킹 배열(검색 시)
    suspend fun getSearchRankingResults(): Response<Map<String, List<String>>>

    @GET("/api/basics/activityAnalysis")//페이지8 사용할 기본 정보 제공
    suspend fun getActivityAnalysis(): Response<Map<String, List<String>>>

    @GET("/members/{chatRoomId}") // 채팅방 멤버 목록 가져오기
    fun getChattingRoomMembers(
        @Path("chatRoomId") chatRoomId: Int
    ): Call<List<String>>

    @GET("/api/wordCloudImageUrl/{chatRoomId}/{userId}") // 특정 사용자의 워드 클라우드 이미지 URL 가져오기
    fun getWordCloudImageUrl(
        @Path("chatRoomId") chatRoomId: Int,
        @Path("userId") userId: Int
    ): Call<String>
}
