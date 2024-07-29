package com.example.talkssogi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class Page2Activity : AppCompatActivity() {

    private lateinit var user_name: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var bottomNavigationView : BottomNavigationView
    private val viewModel: MyViewModel by lazy {
        (application as MyApplication).viewModel
    }

    private lateinit var sharedPreferences: SharedPreferences

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
        //chatRoomAdapter = ChatRoomAdapter(emptyList()) // 초기화는 빈 리스트로
        // Adapter 생성 시 클릭 리스너 전달
        chatRoomAdapter = ChatRoomAdapter(emptyList()) { chatRoom ->
            // 클릭 시 처리할 로직
            val intent = Intent(this, FragmentActivity::class.java)
            intent.putExtra("crnum", chatRoom.crnum) // 채팅방 ID를 전달
            startActivity(intent)
        }
        recyclerView.adapter = chatRoomAdapter

        // SharedPreferences에서 사용자 아이디를 가져온다
        sharedPreferences = getSharedPreferences("Session_ID", Context.MODE_PRIVATE)

        // 실시간으로 변화 확인하면서 화면 출력(=api요청 시 실행 됨)
        viewModel.chatRoomList.observe(this, { chatRooms ->
            Log.d("fetchChatRooms", "옵저버 감지 Received chat rooms: $chatRooms") // 로그 추가
            chatRooms?.let {
                chatRoomAdapter.submitList(it)
            }
        })

        // BottomNavigationView의 아이템 선택 리스너 설정
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_update -> {
                    // 업데이트 선택 시 처리
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

}
