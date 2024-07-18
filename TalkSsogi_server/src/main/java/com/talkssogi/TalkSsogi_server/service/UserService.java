package com.talkssogi.TalkSsogi_server.service;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<String> getAllUserIds() {
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getUserId).collect(Collectors.toList());
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public User findUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public void addChattingRoomToUser(String userId, ChattingRoom room) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            user.getChatList().add(room);
            userRepository.save(user);
        }
    }

    // User 객체의 채팅방 목록을 가져오는 메서드
    public List<ChattingRoom> getChattingRoomsByUserId(String userId) {
        User user = userRepository.findByUserId(userId);
        if (user != null) {
            return user.getChatList();
        }
        return new ArrayList<>(); // 유저가 없을 경우 빈 리스트 반환
    }
}