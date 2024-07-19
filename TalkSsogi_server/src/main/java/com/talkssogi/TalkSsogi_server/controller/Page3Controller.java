package com.talkssogi.TalkSsogi_server.controller;


import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import com.talkssogi.TalkSsogi_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
파일 업로드 및 채팅방 생성을 처리하는건 Page3Service
Page3Service: 이 서비스 클래스는 파일 업로드와 채팅방 생성을 담당합니다. 각 요청마다 사용자와 채팅방을 적절히 관리합니다.
Page3Controller: 이 컨트롤러 클래스는 Page3Service에 핵심 로직을 위임하여 코드의 역할을 명확히 분리하고 유지보수를 용이하게 합니다.
 */

@RestController
@RequestMapping("/api/analysis")
public class Page3Controller {

    private final ChattingRoomService chattingRoomService;
    private final UserService userService;
    private final AnalysisResult analysisResult;


    @Autowired
    public Page3Controller(ChattingRoomService chattingRoomService, UserService userService, AnalysisResult analysisResult) {
        this.chattingRoomService = chattingRoomService;
        this.userService = userService;
        this.analysisResult = analysisResult;
    }

    @PostMapping("/api/uploadfile")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file,
                                             @RequestParam("userId") String userId,
                                             @RequestParam("headcount") int headcount) {
        try {
            String result = chattingRoomService.handleFileUpload(file, userId, headcount);
            return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }
    }
}