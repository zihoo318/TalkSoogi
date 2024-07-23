package com.example.talkssogi

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView

// 테스트용 버튼들
private lateinit var buttonOpenPage5: Button
private lateinit var buttonOpenPage1: Button
private lateinit var user_name: TextView

class Page2Activity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var bottomNavigationView : BottomNavigationView
    private val viewModel: MyViewModel by lazy {
        (application as MyApplication).viewModel
    }

    private lateinit var sharedPreferences: SharedPreferences //intent를 위한 유저 아이

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page2)

        //테스트용 버튼 선언
        buttonOpenPage5 = findViewById(R.id.button_open_page5)
        buttonOpenPage1 = findViewById(R.id.button_open_page1)
        user_name = findViewById(R.id.user_name)    //현재 사용하고 있는 사용자 아이디 출력
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatRoomAdapter = ChatRoomAdapter(emptyList()) // 초기화는 빈 리스트로
        recyclerView.adapter = chatRoomAdapter

        // SharedPreferences에서 사용자 아이디를 가져온다
        sharedPreferences = getSharedPreferences("Session_ID", Context.MODE_PRIVATE)

        viewModel.chatRoomList.observe(this, { chatRooms ->
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
                    val userToken = sharedPreferences.getString("userToken", null)
                    val intent = Intent(this, Page3Activity::class.java)
                    intent.putExtra("userId", userToken)
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

        //////////page5로 가기/////////
        buttonOpenPage5.setOnClickListener {
            val intent = Intent(this, FragmentActivity::class.java)
            startActivity(intent)
        }
        //////////page1로 가기(db확인차)/////////
        buttonOpenPage1.setOnClickListener {
            val intent = Intent(this, Page1Activity::class.java)
            startActivity(intent)
        }

    }
}
