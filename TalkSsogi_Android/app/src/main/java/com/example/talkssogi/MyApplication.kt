package com.example.talkssogi

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class MyApplication : Application() {
    val viewModel: MyViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(this)
            .create(MyViewModel::class.java)
    }
}

data class UserIdResponse(val userIds: List<String>) // 전체 유저 아이디들 페이지1
data class User(val userId: String) //유저 아이디 db저장을 위한 클래스 페이지1
data class Page9SearchData(val selectedDate1: String?, val selectedDate2: String?,
                           val searchWho: String, val resultsItem: String) //페이지9에서 검색할 정보 담기
data class ImageURL(val imageUrl: String) // 서버에서 반환하는 이미지 URL 담아 옴 페이지9

class MyViewModel(application: Application) : AndroidViewModel(application) {
    // private val BASE_URL = "http://10.0.2.2:8080/" // 실제 API 호스트 URL로 대체해야 됨 //에뮬레이터에서 호스트 컴퓨터의 localhost를 가리킴
    private val BASE_URL = "http://172.30.1.84/"  // 실제 안드로이드 기기에서 실행 할 때

    // 테스트 중 원인 분석을 위한 로그 보기 설정 (OkHttpClient 설정)
    val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()


    private val apiService = Retrofit.Builder() //api 사용을 위한 객체
        .baseUrl(BASE_URL)
        .client(client) // OkHttpClient를 Retrofit에 설정 (원인 분석을 위한 로그를 보기위한 설정)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)

    private val _userIds = MutableLiveData<List<String>>() // 전체 유저 아이디 목록
    val userIds: LiveData<List<String>>
        get() = _userIds

    private val _chatRoomList = MutableLiveData<List<ChatRoom>>() // 한 유저의 채팅방 목록
    val chatRoomList: LiveData<List<ChatRoom>>
        get() = _chatRoomList
    //가을 추가 코드
    private val _headcount = MutableLiveData<Int>() // 파일 업로드 버튼 클릭시 인원수
    val headcount: LiveData<Int>
        get() = _headcount

    private val _fileUri = MutableLiveData<String>() // 파일 업로드 버튼 클릭시 파일 URI
    val fileUri: LiveData<String>
        get() = _fileUri
//가을 추가 코드


    init {
        fetchUserIds() // 전체 유저 아이디 목록
        fetchChatRooms() // 한 유저의 채팅방 목록
    }

    fun fetchUserIds() { // 서버에 요청하고 전체 유저 목록 받기 페이지1
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
        val userToken = getUserToken()
        // API 요청하여 채팅방 목록 가져오기
        apiService.getChatRooms(userID = userToken).enqueue(object : Callback<List<ChatRoom>> {
            override fun onResponse(call: Call<List<ChatRoom>>, response: Response<List<ChatRoom>>) {
                if (response.isSuccessful) {
                    // 성공적인 응답(onResponse)일 경우, 받은 채팅방 목록을 처리하여 ViewModel에 값을 설정
                    val chatRooms = response.body()
                    chatRooms?.let { _chatRoomList.value = it }
                } else {
                    // 오류 처리
                    // 예를 들어, response.code() 등을 이용하여 상세 오류 처리
                }
            }

            override fun onFailure(call: Call<List<ChatRoom>>, t: Throwable) {
                // 네트워크 오류 처리
                // 예를 들어, 네트워크 연결 상태 확인 및 사용자에게 오류 메시지 표시
            }
        })
    }

    fun sendUserId(userId: String) { //페이지1에서 쓸 유저 생성(아이디 입력 후 확인 버튼 누르면)
        apiService.sendUserId(User(userId)).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if (response.isSuccessful) {
                    // 성공 처리
                    println("User created successfully")
                } else {
                    // 실패 처리
                    println("Failed to create user: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                // 네트워크 실패 처리
                println("Network error: ${t.message}")
            }
        })
    }

    private fun getUserToken(): String {
        val sharedPreferences: SharedPreferences = getApplication<Application>().getSharedPreferences("Session_ID", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userToken", "") ?: ""
    }

    //파일 업로드 버튼 클릭시 파일과 인원수 부모델에 저장
    fun setHeadCountAndFile(headcount: Int, fileUri: String) {
        _headcount.value = headcount
        _fileUri.value = fileUri
    }

    // 파일 업로드 api실행 메서드 페이지3
    fun uploadFile(fileUri: Uri, userId: String, headcount: Int) {
        // 파일 경로로 File 객체를 생성
        val file = File(fileUri.path ?: "")

        // 파일을 RequestBody로 변환
        val requestFile = file.asRequestBody("multipart/form-data".toMediaType())
        // MultipartBody.Part로 변환
        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)

        // 사용자 아이디와 인원 수를 RequestBody로 변환
        val userIdPart = userId.toRequestBody("text/plain".toMediaType())
        val headcountPart = headcount.toString().toRequestBody("text/plain".toMediaType())

        // API 호출
        apiService.uploadFile(body, userId, headcount).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    println("파일 업로드 성공")
                } else {
                    println("파일 업로드 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                println("네트워크 오류: ${t.message}")
            }
        })
    }

}