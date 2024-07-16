package com.example.talkssogi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class ChatRoom(val name: String, val profileImageResId: Int)

class ChatRoomAdapter(private val chatRoomList: List<ChatRoom>) : RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatRoomList[position]
        holder.chatRoomName.text = chatRoom.name
        holder.profileImage.setImageResource(chatRoom.profileImageResId)
    }

    override fun getItemCount(): Int {
        return chatRoomList.size
    }

    class ChatRoomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profile_image)
        val chatRoomName: TextView = itemView.findViewById(R.id.chat_room_name)
    }
}