package com.talkssogi.TalkSsogi_server.domain;

import java.util.ArrayList;

public class User {
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