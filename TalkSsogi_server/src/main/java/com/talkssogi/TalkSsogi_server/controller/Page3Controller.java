package com.talkssogi.TalkSsogi_server.controller;


import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.repository.ChattingRoomRepository;
import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@RestController
@RequestMapping("/api")
public class Page3Controller {

    private final ChattingRoomService chattingRoomService;
    private final ChattingRoomRepository chattingRoomRepository;
    private static final Logger logger = LoggerFactory.getLogger(Page3Controller.class);

    @Autowired
    public Page3Controller(ChattingRoomService chattingRoomService,ChattingRoomRepository chattingRoomRepository) {
        this.chattingRoomService = chattingRoomService;
        this.chattingRoomRepository = chattingRoomRepository;
    }

    @PostMapping("/uploadfile")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
                                        @RequestParam("userId") String userId,
                                        @RequestParam("headcount") int headcount) {
        try {
            // 업로드된 파일의 MIME 타입이 ZIP 파일인지 확인
            if (file.getContentType() != null && file.getContentType().equals("application/zip")) {
                // ZIP 파일을 처리하는 로직
                List<File> extractedFiles = processZipFile(file);
                logger.info("ZIP file 집파일 둘어옴~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            } else {
                logger.info("NOT ZIP file 일반 파일 들어옴~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                // 일반 파일을 처리하는 로직
                ChattingRoom chattingRoom = chattingRoomService.handleFileUpload(file, userId, headcount);

                // JSON 응답을 위한 Map 생성
                Map<String, Object> response = new HashMap<>();
                response.put("crNum", chattingRoom.getCrNum());
                response.put("filePath", chattingRoom.getFilePath());

                // 성공 응답 반환
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
        } catch (IOException e) {
            // 파일 업로드 실패 시 에러 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패: " + e.getMessage());
        }

        // ZIP 파일 처리 후 적절한 응답 반환
        Map<String, Object> response = new HashMap<>();
        response.put("message", "File processed successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 파일 업데이트 핸들러 메서드
    @PostMapping("/updatefile/{crnum}")
    public ResponseEntity<?> updateFile(@PathVariable("crnum") int crnum,
                                        @RequestParam("file") MultipartFile file) {
        try {
            // 업로드된 파일이 ZIP 파일인지 확인
            if (file.getContentType().equals("application/zip")) {
                // ZIP 파일을 처리하는 로직
                List<File> extractedFiles = processZipFile(file);
                // 압축 해제된 파일들을 처리하는 추가 로직이 필요할 수 있음
            } else {
                // 일반 파일을 직접 처리하는 로직
                processFile(file, crnum);
            }

            // 예시 응답 - 실제 로직으로 대체 필요
            Map<String, Object> response = new HashMap<>();
            response.put("message", "File uploaded successfully");

            // 성공 응답 반환
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (IOException e) {
            // 파일 업데이트 실패 시 에러 응답 반환
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업데이트 실패: " + e.getMessage());
        }
    }

    // 파일을 처리하고 DB에 저장하는 메서드 (기존의 updateFile 로직)
    private void processFile(MultipartFile file, int crnum) {
        try {
            // 서비스 레이어를 통해 파일 정보를 업데이트
            ChattingRoom chattingRoom = chattingRoomService.updateFile(crnum, file);

            // 응답으로 보낼 Map 생성 (실제 응답으로 사용되지는 않음, 예시용)
            Map<String, Object> response = new HashMap<>();
            response.put("crNum", chattingRoom.getCrNum());
            response.put("filePath", chattingRoom.getFilePath());

        } catch (IOException e) {
            // IOException 발생 시 예외 처리
            // 예외 메시지를 출력하거나 로그로 기록
            System.err.println("파일 처리 중 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();

            // 예외를 다시 던져 상위 메서드에서 처리할 수 있도록 할 수도 있습니다.
            throw new RuntimeException("파일 처리 실패: " + e.getMessage(), e);
        } catch (Exception e) {
            // 기타 예외 발생 시 처리
            System.err.println("알 수 없는 오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();

            // 필요에 따라 예외를 다시 던짐
            throw new RuntimeException("파일 처리 중 알 수 없는 오류 발생: " + e.getMessage(), e);
        }
    }

    // ZIP 파일을 처리하여 압축을 푸는 메서드
    private List<File> processZipFile(MultipartFile zipFile) throws IOException {
        // ZIP 파일을 임시 파일로 저장
        File tempFile = File.createTempFile("upload", ".zip");
        zipFile.transferTo(tempFile);

        List<File> extractedFiles = new ArrayList<>();

        // ZIP 파일을 읽어들이기 위한 스트림 생성
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(tempFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            // ZIP 파일 내의 모든 항목을 반복
            while (zipEntry != null) {
                if (!zipEntry.isDirectory()) {
                    // 압축 해제된 파일을 저장할 경로 지정 및 파일 생성
                    File newFile = new File("upload_dir", zipEntry.getName());
                    new File(newFile.getParent()).mkdirs(); // 부모 디렉토리 생성
                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[1024];
                        int len;
                        // ZIP 파일을 읽어서 새 파일로 저장
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }
                    // 압축 해제된 파일을 리스트에 추가
                    extractedFiles.add(newFile);
                }
                // 다음 ZIP 항목으로 이동
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }

        return extractedFiles; // 압축 해제된 파일 목록 반환
    }
}