package com.talkssogi.TalkSsogo_server.service;

import com.talkssogi.TalkSsogo_server.domain.User;
import com.talkssogi.TalkSsogo_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class Page1Service {

    private final UserRepository userRepository;

    @Autowired
    public Page1Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<String> getAllUserIds() {
        List<User> users = userRepository.findAll();
        return users.stream().map(User::getUserId).collect(Collectors.toList());
    }
}