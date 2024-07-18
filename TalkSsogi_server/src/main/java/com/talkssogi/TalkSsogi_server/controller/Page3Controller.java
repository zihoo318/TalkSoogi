package com.talkssogi.TalkSsogi_server.controller;


import com.talkssogi.TalkSsogi_server.service.Page3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/*
파일 업로드 및 채팅방 생성을 처리하는건 Page3Service
Page3Service: 이 서비스 클래스는 파일 업로드와 채팅방 생성을 담당합니다. 각 요청마다 사용자와 채팅방을 적절히 관리합니다.
Page3Controller: 이 컨트롤러 클래스는 Page3Service에 핵심 로직을 위임하여 코드의 역할을 명확히 분리하고 유지보수를 용이하게 합니다.
 */

@RestController
public class Page3Controller {

    private final Page3Service page3Service;

    @Autowired
    public Page3Controller(Page3Service page3Service) {
        this.page3Service = page3Service;
    }

    @PostMapping("/api/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("userId") String userId) {
        try {
            String result = page3Service.handleFileUpload(file, userId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to upload file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/api/addChatRoom")
    public ResponseEntity<String> addChatRoom(@RequestParam("file") MultipartFile file,
                                              @RequestParam("headcount") int headcount,
                                              @RequestParam("userId") String userId) {
        try {
            String result = page3Service.addChatRoom(file, headcount, userId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to add chat room: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}