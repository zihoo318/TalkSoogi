package com.example.talkssogi

import com.example.talkssogi.model.ChatRoom
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class Page2Activity : AppCompatActivity() {

    private lateinit var user_name: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: MyViewModel by lazy {
        (application as MyApplication).viewModel
    }

    private lateinit var sharedPreferences: SharedPreferences

    // Retrofit 설정 및 ApiService 인터페이스 생성
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL) // 서버의 기본 URL을 Constants에서 가져옵니다.
        .addConverterFactory(GsonConverterFactory.create()) // JSON 변환을 위해 Gson을 사용합니다.
        .build()

    private val apiService: ApiService = retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page2)

        // SharedPreferences에서 사용자 아이디를 가져온다
        sharedPreferences = getSharedPreferences("Session_ID", Context.MODE_PRIVATE)

        // user_name TextView 초기화
        user_name = findViewById(R.id.user_name)

        // SharedPreferences에서 사용자 아이디를 가져온다
        val userId = sharedPreferences.getString("Session_ID", "Unknown User")

        // 사용자 아이디를 TextView에 설정
        user_name.text = userId

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Adapter 생성 시 클릭 리스너 전달
        chatRoomAdapter = ChatRoomAdapter(emptyList()) { chatRoom ->
            // 클릭 시 처리할 로직
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("chatRoomId", chatRoom.crnum) // 채팅방 ID를 전달
            startActivity(intent)
        }
        recyclerView.adapter = chatRoomAdapter

        // 실시간으로 변화 확인하면서 화면 출력(=api요청 시 실행 됨)
        viewModel.chatRoomList.observe(this) { chatRooms ->
            Log.d("fetchChatRooms", "옵저버 감지 Received chat rooms: $chatRooms") // 로그 추가
            chatRooms?.let {
                chatRoomAdapter.submitList(it)
            }
        }

        // BottomNavigationView의 아이템 선택 리스너 설정
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_update -> {
                    // 업데이트 선택 시 처리
                    showUpdateDialog()
                    true
                }

                R.id.navigation_add -> {
                    // 추가 선택 시 처리: Page3Activity로 이동
                    val userToken = sharedPreferences.getString("Session_ID", null)
                    val intent = Intent(this, Page3Activity::class.java)
                    intent.putExtra("Session_ID", userToken)
                    startActivity(intent)
                    true
                }

                R.id.navigation_edit -> {
                    // 편집 선택 시 처리(채팅방 삭제)
                    true
                }

                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // 데이터 갱신
        viewModel.fetchChatRooms()
        Log.d("fetchChatRooms", "2에서 resume으로 갱신 Number of chat rooms")
    }

    private fun showUpdateDialog() {
        // 다이얼로그 레이아웃을 Inflate 합니다.
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_chatroom_selection, null)
        val recyclerViewDialog = dialogView.findViewById<RecyclerView>(R.id.recyclerViewChatRooms)

        // 다이얼로그에 사용할 Adapter 생성
        val dialogChatRoomAdapter = DialogChatRoomAdapter(emptyList()) { chatRoom ->
            // 채팅방 클릭 시 처리할 로직을 여기에 추가 가능
        }

        recyclerViewDialog.layoutManager = LinearLayoutManager(this)
        recyclerViewDialog.adapter = dialogChatRoomAdapter

        // ViewModel을 통해 채팅방 목록을 가져와서 Adapter에 설정
        viewModel.chatRoomList.observe(this) { chatRooms ->
            chatRooms?.let {
                dialogChatRoomAdapter.submitList(it)
            }
        }

        // 다이얼로그 생성
        val dialog = AlertDialog.Builder(this)
            .setTitle("채팅방 선택")
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                val selectedCrnum = dialogChatRoomAdapter.selectedCrnum
                if (selectedCrnum != null) {
                    // 선택된 채팅방의 crnum을 로그로 출력
                    Log.d("SelectedChatRoom", "선택된 채팅방의 crnum: $selectedCrnum")
                    // 서버로 선택한 crnum 전송
                    sendCrnumToServer(selectedCrnum)
                } else {
                    Log.d("SelectedChatRoom", "선택된 채팅방이 없습니다.") // 선택된 채팅방이 없는 경우 로그 출력
                }
            }
            .setNegativeButton("취소", null)
            .create()

        dialog.show()
    }

    private fun sendCrnumToServer(crnum: Int) {
        // ApiService의 메서드를 호출하여 서버로 요청을 보냅니다.
        val call = apiService.runBasicPythonAnalysis(crnum)

        // 비동기적으로 요청을 실행하고 콜백을 통해 응답을 처리합니다.
        call.enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if (response.isSuccessful) {
                    // 요청이 성공적으로 처리된 경우
                    val result = response.body() ?: "결과를 받을 수 없습니다."
                    Log.d("AnalysisResult", "분석 결과: $result")

                    // 분석 결과를 처리하는 로직을 추가합니다.
                    handleAnalysisResult(result)
                } else {
                    // 요청이 실패한 경우
                    Log.e("AnalysisResult", "분석 요청 실패: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                // 네트워크 오류 또는 요청 실패
                Log.e("AnalysisResult", "네트워크 오류: ${t.message}")
            }
        })
    }

    // 분석 결과를 처리하는 함수
    private fun handleAnalysisResult(result: String) {
        // 결과를 처리하거나 사용자에게 표시하는 로직을 여기에 추가합니다.
        // 예: 대화 상자를 표시하거나 화면에 결과를 보여주는 코드
        AlertDialog.Builder(this)
            .setTitle("분석 결과")
            .setMessage(result)
            .setPositiveButton("확인", null)
            .show()
    }
}
