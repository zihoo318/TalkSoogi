package com.talkssogi.TalkSsogi_server.controller;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public ResponseEntity<Map<String, Object>> login(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        if (userService.userIdExistsForPage1(userId)) {
            User user = userService.findUserById(userId);
            Set<ChattingRoom> chatRooms = userService.getChattingRoomsByUserId(userId);
            response.put("user", user);
            response.put("chatRooms", chatRooms);
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User not found");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestParam String userId) {
        Map<String, Object> response = new HashMap<>();
        if (!userService.userIdExistsForPage1(userId)) {
            User newUser = new User();
            newUser.setUserId(userId);
            userService.addUser(newUser);
            response.put("user", newUser);
            response.put("chatRooms", new HashSet<>());
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "User ID already exists");
            return ResponseEntity.status(409).body(response);
        }
    }

    @GetMapping("/checkUserId")
    public ResponseEntity<String> checkUserId(@RequestParam String userId) {
        if (userService.userIdExistsForPage1(userId)) {
            return ResponseEntity.ok("사용 중인 아이디입니다");
        } else {
            return ResponseEntity.ok("사용 가능한 아이디입니다");
        }
    }

}
