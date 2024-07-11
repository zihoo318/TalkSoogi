package com.example.talkssogi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Page2Activity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private lateinit var chatRoomList: List<ChatRoom>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page2)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // 카톡방 데이터 예시
        chatRoomList = listOf(
            ChatRoom("카톡방 1", R.drawable.profile_placeholder),
            ChatRoom("카톡방 2", R.drawable.profile_placeholder),
            ChatRoom("카톡방 3", R.drawable.profile_placeholder),
            ChatRoom("카톡방 4", R.drawable.profile_placeholder),
            ChatRoom("카톡방 5", R.drawable.profile_placeholder),
            ChatRoom("카톡방 6", R.drawable.profile_placeholder),
            ChatRoom("카톡방 7", R.drawable.profile_placeholder)
        )

        chatRoomAdapter = ChatRoomAdapter(chatRoomList)
        recyclerView.adapter = chatRoomAdapter
    }
}
