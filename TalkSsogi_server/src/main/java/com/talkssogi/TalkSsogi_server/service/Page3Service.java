package com.talkssogi.TalkSsogi_server.service;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class Page3Service {

    private static final String UPLOAD_DIR = "/path/to/your/upload/directory";
    private final UserRepository userRepository;

    @Autowired
    public Page3Service(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String handleFileUpload(MultipartFile file, String userId) throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
        File destFile = new File(filePath);
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = new User(userId);
            userRepository.save(user);
        }

        ChattingRoom newRoom = new ChattingRoom();
        newRoom.setFilePath(filePath);
        newRoom.setHeadcount(0);
        user.addChatRoom(newRoom);

        return "File uploaded successfully";
    }

    public String addChatRoom(MultipartFile file, int headcount, String userId) throws IOException {
        if (file.isEmpty()) {
            return "File is empty";
        }

        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String filePath = UPLOAD_DIR + File.separator + file.getOriginalFilename();
        File destFile = new File(filePath);
        try (InputStream inputStream = file.getInputStream();
             FileOutputStream outputStream = new FileOutputStream(destFile)) {

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = new User(userId);
            userRepository.save(user);
        }

        ChattingRoom newRoom = new ChattingRoom();
        newRoom.setFilePath(filePath);
        newRoom.setHeadcount(headcount);
        user.addChatRoom(newRoom);

        return "Chat room added successfully";
    }
}