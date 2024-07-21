package com.talkssogi.TalkSsogi_server.domain;

import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "user")
public class User {

    @Id //기본키
    private String userId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    User가 여러 개의 ChattingRoom와 관계를 맺고 있다
//    ChattingRoom 엔티티의 user 필드에 의해 매핑된다 chattingRoom에서 외래 키를 관리하고 User는 관계를 읽기만
//    User 엔티티에 대한 변경이 ChattingRoom 엔티티에 전파된다
//    즉시 로드되지 않고 필요할 때  로드
    private ArrayList<ChattingRoom> chatList = new ArrayList<>();

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