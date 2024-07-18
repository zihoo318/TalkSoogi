package com.talkssogi.TalkSsogi_server.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.util.ArrayList;

@Entity
@Table(name = "users")
public class User {

    @Id //기본키
    private String userId;

    private ArrayList<ChattingRoom> chatList;

    public User(String userId) {
        this.userId = userId;
        this.chatList = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public ArrayList<ChattingRoom> getChatList() {
        return chatList;
    }

    public void addChatRoom(ChattingRoom room) {
        this.chatList.add(room);
    }

}