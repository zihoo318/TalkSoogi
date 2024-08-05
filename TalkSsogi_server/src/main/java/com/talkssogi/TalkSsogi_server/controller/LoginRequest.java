package com.talkssogi.TalkSsogi_server.domain;

public class LoginRequest {
    private String userId;

    public LoginRequest() {
    }

    public LoginRequest(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
