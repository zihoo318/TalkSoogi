package com.talkssogi.TalkSsogi_server.controller;

import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
Page1Service를 모든 유저의 id를 저장한 배열을 만드는 함수를 포함해서 만들어줘 이 함수를 사용해서 Page1Controller에서 api로 보내도록
Page1Service랑 Page1Controller 만들기!!
 */

@RestController
@RequestMapping("/api")
public class Page1Controller {

    private final UserService userService;


    @Autowired
    public Page1Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userIds")  // 기존에 있는 아이디들 목록으로 보내주기(for 아이디 중복 확인)
    public ResponseEntity<List<String>> getAllUserIds() {
        List<String> userIds = userService.getAllUserIds();
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    @PostMapping("/userId")
    public ResponseEntity<String> createUser(@RequestParam("userId") String userId) {
        // 새로운 사용자 객체를 생성
        User newUser = new User(userId);

        // UserService를 통해 사용자 저장
        userService.addUser(newUser);

        // 성공 응답 반환
        return ResponseEntity.ok("User created successfully");
    }
}