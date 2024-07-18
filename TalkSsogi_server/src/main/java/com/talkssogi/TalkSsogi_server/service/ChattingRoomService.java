package com.talkssogi.TalkSsogi_server.service;

import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.domain.User;
import com.talkssogi.TalkSsogi_server.repository.ChattingRoomRepository;
import com.talkssogi.TalkSsogi_server.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
@Transactional
public class ChattingRoomService {

    private static final String UPLOAD_DIR = "C:/Users/Master/TalkSsogi_Workspace/"; //테스트용 경로

    private final ChattingRoomRepository chattingRoomRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChattingRoomService(ChattingRoomRepository chattingRoomRepository, UserRepository userRepository) {
        this.chattingRoomRepository = chattingRoomRepository;
        this.userRepository = userRepository;
    }

    public String handleFileUpload(MultipartFile file, String userId, int headcount) throws IOException {
        // MultipartFile을 받아 파일 업로드 처리
        // 파일 업로드 성공 여부에 따라 적절한 응답을 반환

        // 유저 확인 및 생성
        User user = userRepository.findByUserId(userId);
        if (user == null) {
            user = new User(userId);
            userRepository.save(user);
        }

        if (file.isEmpty()) {
            return "업로드할 파일을 선택하세요.";
        }

        try {
            // 파일 저장 경로 설정
            Path uploadPath = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            // 파일 저장
            Files.write(uploadPath, file.getBytes());
            // 새로운 채팅방 객체 생성
            ChattingRoom newRoom = new ChattingRoom(uploadPath.toString(), headcount);
            user.addChatRoom(newRoom);
            // 채팅방 저장
            chattingRoomRepository.save(newRoom);
            // 파일 저장 후 반환할 응답 메시지 설정
            return "파일 업로드 성공";
        } catch (Exception e) {
            // 파일 저장 실패 시 예외 처리
            return "파일 업로드 실패: " + e.getMessage();
        }

    }

    public void updateAnalysisResult(String filePath, AnalysisResult result) {
        // 파일 경로를 사용하여 채팅방 객체 조회
        ChattingRoom room = chattingRoomRepository.findByFilePath(filePath);
        if (room != null) {
            // 채팅방 객체에 분석 결과 설정
            room.setAnalysisResult(result);
            // 채팅방 저장
            chattingRoomRepository.save(room);
        }
    }
}