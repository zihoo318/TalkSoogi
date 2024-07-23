package com.talkssogi.TalkSsogi_server.service;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.repository.ChattingRoomRepository;
import com.talkssogi.TalkSsogi_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChattingRoomRepository chattingRoomRepository;

    public List<String> getAllUserIds() {
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getUserId).collect(Collectors.toList());
    }

    // 사용자 ID 존재 여부 확인(페이지7에서 사용)
    public boolean userIdExists(String userId) {
        return getAllUserIds().contains(userId);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User findUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    // User 객체의 채팅방 목록을 가져오는 메서드
    public Set<ChattingRoom> getChattingRoomsByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            // List를 Set으로 변환하여 반환
            return new HashSet<>(user.getChatList());
        }
        return new HashSet<>(); // 유저가 없을 경우 빈 Set 반환
    }

    // 유저에게 채팅방 생성 및 추가
    // 무조건 ChattingRoomService의 handleFileUpload를 실행하고 파일경로를 받아서 이 메서드 실행
    public ChattingRoom createChattingRoomForUser(String userId, String filePath, int headcount) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // 새 채팅방 생성
        ChattingRoom newRoom = new ChattingRoom();
        newRoom.setFilePath(filePath);
        newRoom.setHeadcount(headcount);

        // 채팅방 번호 생성 및 할당
        int nextRoomNumber = generateNextChattingRoomNumber(user);
        newRoom.setCrNum(nextRoomNumber);

        // 채팅방을 유저의 채팅방 목록에 추가
       // user.addChatRoom(newRoom);
        user.getChatList().add(newRoom);
        newRoom.setUser(user);

        // 데이터베이스에 저장
        chattingRoomRepository.save(newRoom);
        userRepository.save(user);

        return newRoom;
    }

    private int generateNextChattingRoomNumber(User user) {
        // 유저의 채팅방 목록에서 가장 큰 번호를 찾고, 그 다음 번호를 반환
        Set<ChattingRoom> chatRooms = user.getChatList();
        int count = chatRooms.size(); // 채팅방 개수를 카운트
        return count + 1; // 다음 채팅방 번호 반환
    }
}