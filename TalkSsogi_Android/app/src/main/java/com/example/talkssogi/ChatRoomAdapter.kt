package com.example.talkssogi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ChatRoom(val crnum: Int, val name: String)

class ChatRoomAdapter(private var chatRoomList: List<ChatRoom>) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRoomList[position]
        holder.chatRoomName.text = chatRoom.name
        holder.profileImage.setImageResource(R.drawable.profile_placeholder)
    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        val chatRoomName: TextView = itemView.findViewById(R.id.chat_room_name)
    }

    fun submitList(newChatRooms: List<ChatRoom>) {
        chatRoomList = newChatRooms
        notifyDataSetChanged() // 데이터가 변경되었음을 어댑터에 알림
    }
}