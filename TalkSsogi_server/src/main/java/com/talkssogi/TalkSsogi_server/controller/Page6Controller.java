package com.talkssogi.TalkSsogi_server.controller;


import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
import com.talkssogi.TalkSsogi_server.service.AnalysisResultService;
import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import com.talkssogi.TalkSsogi_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
page6Service에서 파이썬 코드를 실행해서 생성된 AnalysisResult의 wordCloudImageUrl을 전달하도록 할건데
아직 파이썬 코드를 못 만들어서 임시로 wordCloudImageUrl에 값이 있다고 치고 전달하도록 서비스파일이랑 컨트롤러 파일을 만들기
=>안드로이드 스튜디오에서 만든 코드 다시 보고 어떤 형태로 데이터를 넘겨줘야하는지 고려해서 api만들기
 */

@RestController
@RequestMapping("/api")
public class Page6Controller {

    private final ChattingRoomService chattingRoomService;
    private final UserService userService;
    private final AnalysisResultService analysisResultService;

    @Autowired
    public Page6Controller(ChattingRoomService chattingRoomService, UserService userService, AnalysisResultService analysisResultService) {
        this.chattingRoomService = chattingRoomService;
        this.userService = userService;
        this.analysisResultService = analysisResultService;
    }

    @GetMapping("/members/{chatRoomId}")
    public ResponseEntity<List<String>> getChattingRoomMembers(@PathVariable Integer chatRoomId) {
        List<String> members = chattingRoomService.getChattingRoomMembers(chatRoomId);
        if (members.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/wordCloudImageUrl/{chatRoomId}/{userId}")
    public ResponseEntity<String> getWordCloudImageUrl(@PathVariable Integer chatRoomId, @PathVariable String userId) {
        String wordCloudImageUrl = analysisResultService.findWordCloudImageUrlByChatRoomIdAndUserId(chatRoomId, userId);
        if (wordCloudImageUrl == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(wordCloudImageUrl, HttpStatus.OK);
    }
}