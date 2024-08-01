package com.talkssogi.TalkSsogi_server.controller;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class Page1Controller {

    private final UserService userService;

    @Autowired
    public Page1Controller(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userIds")
    public ResponseEntity<List<String>> getAllUserIds() {
        List<String> userIds = userService.getAllUserIds();
        return new ResponseEntity<>(userIds, HttpStatus.OK);
    }

    @PostMapping("/userId")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        String userId = user.getUserId();
        if (userService.userIdExistsForPage1(userId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User ID already exists");
        }
        User newUser = new User(userId);
        userService.addUser(newUser);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody User user) {
        String userId = user.getUserId();
        if (userService.userIdExistsForPage1(userId)) {
            User existingUser = userService.findUserById(userId);
            Set<ChattingRoom> chatList = existingUser.getChatList();
            return ResponseEntity.ok("Login successful. Chat rooms: " + chatList.size());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user ID");
        }
    }
}
