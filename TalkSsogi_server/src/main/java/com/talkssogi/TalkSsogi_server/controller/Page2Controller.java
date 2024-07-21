package com.talkssogi.TalkSsogi_server.controller;


import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.service.AnalysisResultService;
import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import com.talkssogi.TalkSsogi_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

//페이지2의 채팅방 목록에 번호 매겨서 스프링부트 유저 객체에 저장된 채팅방 배열의 번호를 갖게 해야됨
// api에 같이 전달해서 해당 채팅방에 대한 결과를 전달 받음
/*
page2 서버에서 가져올 데이터
- 사용자 이름(id) - user_id
- 사용자의 기존 카톡 채팅방 파일 목록 (리스트 형식) - user_chat
=>컨트롤러에서 id랑 목록을 안드로이드로 전달할 api를 만듦
=>서비스에서 전달할 목록을 만들거임
목록 = Map형식으로 key=채팅방 이름, value=채팅방 번호(user.chatList에 저장된 인덱스)
위의 과정에서 필요한게 있으면 도메인이나 레포지토리에 추가해도 됨
=>안드로이드 스튜디오에서 만든 코드 다시 보고 어떤 형태로 데이터를 넘겨줘야하는지 고려해서 api만들기
 */
@RestController
@RequestMapping("/api")
public class Page2Controller {

    private final ChattingRoomService chattingRoomService;
    private final UserService userService;
    private final AnalysisResultService analysisResultService;

    @Autowired
    public Page2Controller(ChattingRoomService chattingRoomService, UserService userService, AnalysisResultService analysisResultService) {
        this.chattingRoomService = chattingRoomService;
        this.userService = userService;
        this.analysisResultService = analysisResultService;
    }

    @GetMapping("/chatrooms") // 채팅방 목록 보내기
    public ResponseEntity<Map<Integer, String>> getChatRooms(@RequestParam String ID) {
        // 특정 사용자의 정보를 가져옵니다.
        User user = userService.findUserById(ID);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 사용자의 채팅방 목록을 가져옵니다.
        Set<ChattingRoom> chatRooms = user.getChatList();
        // 반환할 채팅방 목록을 저장할 Map을 선언합니다.
        Map<Integer, String> chatRoomsMap = new HashMap<>();

        // 각 채팅방에 대해 번호와 이름을 Map에 추가합니다.
        for (ChattingRoom room : chatRooms) {
            Integer roomNumber = room.getCrNum(); // 채팅방 번호 가져오기
            String roomName = room.getAnalysisResult() != null
                    ? room.getAnalysisResult().getChatroomName()
                    : "분석을 실행해주세요"; // 채팅방 이름 가져오기 (이름이 없는 경우의 설정)
            chatRoomsMap.put(roomNumber, roomName); // 채팅방 번호와 이름을 Map에 추가합니다.
        }

        // 생성된 채팅방 목록을 HttpStatus OK와 함께 ResponseEntity로 반환합니다.
        return new ResponseEntity<>(chatRoomsMap, HttpStatus.OK);
    }

}
