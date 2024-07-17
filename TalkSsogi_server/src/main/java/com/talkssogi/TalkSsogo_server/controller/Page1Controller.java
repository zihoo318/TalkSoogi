package com.talkssogi.TalkSsogo_server.controller;

import com.talkssogi.TalkSsogo_server.service.Page1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
Page1Service를 모든 유저의 id를 저장한 배열을 만드는 함수를 포함해서 만들어줘 이 함수를 사용해서 Page1Controller에서 api로 보내도록
Page1Service랑 Page1Controller 만들기!!
 */

@RestController
public class Page1Controller {

    private final Page1Service page1Service;

    @Autowired
    public Page1Controller(Page1Service page1Service) {
        this.page1Service = page1Service;
    }

    @GetMapping("/api/userIds")
    public ResponseEntity<List<String>> getAllUserIds() {
        List<String> userIds = page1Service.getAllUserIds();
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }
}