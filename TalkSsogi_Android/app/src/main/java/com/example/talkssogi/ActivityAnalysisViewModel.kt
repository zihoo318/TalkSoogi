package com.example.talkssogi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory


class ActivityAnalysisViewModel : ViewModel() {
    //private val BASE_URL = "http://10.0.2.2:8080/" // 실제 API 호스트 URL로 대체해야 됨 //에뮬레이터에서 호스트 컴퓨터의 localhost를 가리킴
    private val BASE_URL = "http://192.168.45.165:8080/"  // 실제 안드로이드 기기에서 실행 할 때

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

    var gson = GsonBuilder().setLenient().create()
    private val apiService = Retrofit.Builder() //api 사용을 위한 객체
        .baseUrl(Constants.BASE_URL)
        .client(client) // OkHttpClient를 Retrofit에 설정 (원인 분석을 위한 로그를 보기위한 설정)
        .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 변환
        .build()
        .create(ApiService::class.java)


    private val _imageUrls = MutableLiveData<List<ImageURL>>() //페이지9 이미지url
    val imageUrls: LiveData<List<ImageURL>> get() = _imageUrls

    private val _participants = MutableLiveData<List<String>>() // 페이지9 대화 참가자 이름 리스트
    val participants: LiveData<List<String>> get() = _participants

    // 페이지6 워드 클라우드 이미지 URL, LiveData 변수를 초기화
    private val _wordCloudImageUrl = MutableLiveData<List<ImageURL>>(emptyList()) // 빈 리스트로 초기화
    val wordCloudImageUrl: LiveData<List<ImageURL>> get() = _wordCloudImageUrl

    // 페이지8 기본제공 데이터 값
    private val _messageCountResult = MutableLiveData<String>()
    val messageCountResult: LiveData<String> get() = _messageCountResult
    private val _zeroCountResult = MutableLiveData<String>()
    val zeroCountResult: LiveData<String> get() = _zeroCountResult
    private val _hourlyCountResult = MutableLiveData<String>()
    val hourlyCountResult: LiveData<String> get() = _hourlyCountResult


    //페이지9에서 쓸 검색 정보 보내고 이미지 주소 받기
    fun getActivityAnalysisImage(
        startDate: String?,
        endDate: String?,
        searchWho: String,
        resultsItem: String,
        crnum: Int
    ) {
        if (startDate != null && endDate != null && searchWho.isNotEmpty() && resultsItem.isNotEmpty()) {
            apiService.getActivityAnalysisImage(startDate, endDate, searchWho, resultsItem, crnum)
                .enqueue(object : Callback<String> {
                    override fun onResponse(
                        call: Call<String>,
                        response: Response<String>
                    ) {
                        if (response.isSuccessful) {
                            val imageUrl = response.body()
                            imageUrl?.let {
                                _imageUrls.value = listOf(ImageURL(it))
                            }
                            Log.d("Page9", "이미지 생성 및 전달 받기 성공 : $imageUrl")
                        } else {
                            Log.d("Page9", "이미지 생성 및 전달 받기 실패: ${response.errorBody()?.string()}")
                        }
                    }

                    override fun onFailure(call: Call<String>, t: Throwable) {
                        Log.d("Page9", "이미지 생성 및 전달 받기 실패 네트워크 에러: ${t.message}")
                        t.printStackTrace()
                    }
                })
        }
    }

    // 채팅방 대화 참여자 이름 가져와서 리스트로 변환
    fun loadParticipants(crnum: Int) {
        viewModelScope.launch {
            try {
                Log.d("Page9", "참여자 가져오는 API 호출 직전")
                val response = apiService.getChattingRoomMembers(crnum)
                if (response.isSuccessful) {
                    Log.d("Page9", "참여자 가져오는 API 호출 성공")
                    val participantsList = response.body() ?: emptyList()
                    _participants.postValue(participantsList)
                } else {
                    Log.e("Page9", "API 호출 실패: ${response.errorBody()?.string()}")
                    _participants.postValue(emptyList())
                }
            } catch (e: Exception) {
                Log.e("Page9", "API 호출 예외 발생", e)
                _participants.postValue(emptyList())
            }
        }
    }


    //
    /*페이지 8의 기본 동작(가을 동작)
    suspend fun getActivityAnalysis(): Map<String, List<String>> {
        return try {
            val response = apiService.getActivityAnalysis()
            response.body() ?: emptyMap()
        } catch (e: Exception) {
            Log.e("RankingRepository", "Error fetching search ranking results", e)
            emptyMap()
        }
    }

}*/
    // crnum을 매개변수로 받아서 API 호출 // 페이지8에서 기본제공 활동분석 결과 받기
    fun fetchActivityAnalysis(crnum: Int, callback: (List<String>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // API 호출
                val call = apiService.getActivityAnalysis(crnum)
                call.enqueue(object : Callback<List<String>> {
                    override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                        // 메인 스레드에서 결과 처리
                        viewModelScope.launch(Dispatchers.Main) {
                            if (response.isSuccessful) {
                                Log.e("Page9", "기본제공분석 api응답 받음")
                                val body = response.body() ?: emptyList()
                                callback(body)
                            } else {
                                Log.e("Page9", "Error: ${response.code()}")
                                callback(emptyList())
                            }
                        }
                    }

                    override fun onFailure(call: Call<List<String>>, t: Throwable) {
                        // 메인 스레드에서 예외 처리
                        viewModelScope.launch(Dispatchers.Main) {
                            Log.e("Page9", "Exception: ${t.message}", t)
                            callback(emptyList())
                        }
                    }
                })
            } catch (e: Exception) {
                // 예외 처리
                Log.e("Page9", "Exception: ${e.message}", e)
                viewModelScope.launch(Dispatchers.Main) {
                    callback(emptyList())
                }
            }
        }
    }

    // 위의 메서드를 통해 받은 리스트 데이터를 받아서 LiveData에 설정하는 메소드
    fun fetchAndSetActivityAnalysis(crnum: Int) {
        // fetchActivityAnalysis 호출 시, 결과를 처리하기 위한 callback 정의
        fetchActivityAnalysis(crnum) { resultList ->
            // 값이 있으면 각 LiveData에 설정
            _messageCountResult.value = if (resultList.size > 0) resultList[0] else ""
            _zeroCountResult.value = if (resultList.size > 1) resultList[1] else ""
            _hourlyCountResult.value = if (resultList.size > 2) resultList[2] else ""
        }
    }



    // 워드 클라우드 이미지 URL 가져오기
    fun loadWordCloudImageUrl(crnum: Int, userId: Int) {
        viewModelScope.launch {
            try {
                // activityAnalysisViewModel.getWordCloudImageUrl 호출 후, imageUrls가 wordCloudImageUrl로 수정됨
                val response = apiService.getWordCloudImageUrl(crnum, userId).execute()
                if (response.isSuccessful) {
                    val wordCloudUrls = response.body() ?: emptyList() // 응답이 null이면 빈 리스트로 대체
                    _wordCloudImageUrl.postValue(wordCloudUrls) // 빈 리스트를 설정
                } else {
                    _wordCloudImageUrl.postValue(emptyList()) // 실패 시 빈 리스트로 설정
                }
            } catch (e: Exception) {
                _wordCloudImageUrl.postValue(emptyList()) // 네트워크 오류 시 빈 리스트로 설정
            }
        }
    }
}