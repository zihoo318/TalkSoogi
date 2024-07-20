package com.example.talkssogi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.activity.viewModels


class Page2Activity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private val viewModel: MyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page2)


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        chatRoomAdapter = ChatRoomAdapter(emptyList()) // 초기화는 빈 리스트로
        recyclerView.adapter = chatRoomAdapter

        viewModel.chatRoomList.observe(this, { chatRooms ->
            chatRooms?.let {
                chatRoomAdapter.submitList(it)
            }
        })
    }
}
