package com.talkssogi.TalkSsogi_server.service;


import com.talkssogi.TalkSsogi_server.repository.ChattingRoomRepository;
import com.talkssogi.TalkSsogi_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Page6Service {

    private final UserRepository userRepository;
    private final ChattingRoomRepository chattingRoomRepository;

    @Autowired
    public Page6Service(UserRepository userRepository, ChattingRoomRepository chattingRoomRepository) {
        this.userRepository = userRepository;
        this.chattingRoomRepository = chattingRoomRepository;
    }

//    public List<String> getChattingRoomMembers(Long chatRoomId) {
//        ChattingRoom chattingRoom = chattingRoomRepository.findById(chatRoomId).orElse(null);
//        if (chattingRoom == null) {
//            return new ArrayList<>();
//        }
//        // 예시로 채팅방에 있는 사용자 이름 리스트를 반환
//        return chattingRoom.getUsers().stream()
//                .map(User::getUserId)
//                .collect(Collectors.toList());
//    }

    public String getWordCloudImageUrl(Long chatRoomId) {
        // 임시로 wordCloudImageUrl 값을 반환
        return "http://example.com/wordCloudImageUrl.png";
    }
}