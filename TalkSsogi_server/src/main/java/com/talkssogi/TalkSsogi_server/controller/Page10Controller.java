package com.talkssogi.TalkSsogi_server.controller;

import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.talkssogi.TalkSsogi_server.StorageUploader.S3DownLoader;
import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Page10Controller {

    private static final Logger logger = LoggerFactory.getLogger(Page10Controller.class);

    @Autowired
    private ChattingRoomService chattingRoomService;
    @Autowired
    private S3DownLoader s3DownLoader;

    private static final String PYTHON_INTERPRETER_PATH = "/Python-3.12.0/python";
    private static final String PYTHON_SCRIPT_BASIC_PATH = "/home/ec2-user/workspace/caller_prediction1.py";

    @GetMapping("/caller-prediction-exante")
    public ResponseEntity<String> getCallerPrediction(@RequestParam("crnum") Integer crnum) {
        ChattingRoom chattingRoom = chattingRoomService.findByCrNum(crnum);  // 채팅방 찾기

        String mapping = null;  // 발신자 매핑 결과
        StringBuilder messagesBuilder = new StringBuilder();  // 모든 발신자의 메시지를 저장할 빌더

        try {
            // 발신자 수 얻기
            int headcount = chattingRoom.getHeadcount();
            List<String> memberNames = chattingRoom.getMemberNames();  // 발신자 이름 리스트

            // 발신자별 메시지를 저장할 리스트
            List<String> senderMessages = new ArrayList<>();

            logger.info("caller_prediction 마오짱짱맨!!!!!!!!!! basicActivityAnalysis: " + chattingRoom.getBasicActivityAnalysis());

            // 발신자 인덱스를 기준으로 S3에서 파일을 읽고 메시지를 가져옴
            for (int i = 0; i < headcount; i++) {
                String key = "text-files/" + crnum + "_" + i + "_personal.txt";
                InputStream fileStream = s3DownLoader.getFileStream(key);

                // 파일에서 상위 10문장 추출
                String senderMessage = extractTopAndBottomMessages(fileStream);

                // 발신자 이름과 메시지를 구분자로 연결하여 추가
                String formattedMessage = "<@@@@@" + memberNames.get(i) + "@@@@@>" + senderMessage.replace("\n", "@@@>");
                senderMessages.add(formattedMessage);
            }

            // messages 변수에 각 발신자의 메시지를 '|||>' 구분자로 연결하여 저장
            String messages = String.join("|||>", senderMessages);
            // 명령어 설정
            String command = String.format("%s %s \'%s\'", PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_BASIC_PATH, messages);
            logger.info("page10 messages in command "+messages);
            logger.info("Executing command: " + command);

            // ProcessBuilder를 사용하여 프로세스 생성
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");

            // 프로세스 시작
            Process process = processBuilder.start();

            // 프로세스의 출력 읽기
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            StringBuilder resultBuilder = new StringBuilder();
            String line;
            boolean resultStarted = false;

            while ((line = reader.readLine()) != null) {
                if (line.equals("다음 줄부터 result출력")) {
                    resultStarted = true;
                } else if (resultStarted) {
                    resultBuilder.append(line).append("\n");
                }
            }

            // 표준 오류 스트림 읽기
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));
            StringBuilder errorResult = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorResult.append(line).append("\n");
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            if (exitCode != 0) {
                logger.error("발신자예측 사전준비에서 에러남 Python script error output: " + errorResult.toString());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Script execution failed.");
            }

            logger.info("caller_prediction 결과 출력 전 화긴!!!!!!!!!! basicActivityAnalysis: " + chattingRoom.getBasicActivityAnalysis());

            // 파이썬 스크립트의 결과를 하나의 문자열로 얻음
            mapping = resultBuilder.toString().trim();  // 결과를 공백 제거 후 저장
            logger.info("Python script result: " + mapping);

        } catch (Exception e) {
            logger.error("발신자예측 사전준비에서 에러남 Error executing Python script", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error executing Python script");
        }

        logger.info("caller_prediction set 전에 화긴!!!!!!!!!! basicActivityAnalysis: " + chattingRoom.getBasicActivityAnalysis());

        // 발신자 매핑 결과를 DB에 저장
        if (chattingRoom != null) {
            logger.info("caller_prediction 저장 전에 화긴!!!!!!!!!! basicActivityAnalysis: " + chattingRoom.getBasicActivityAnalysis());
            chattingRoom.setCallerPrediction(mapping);  // 파이썬 결과를 매핑으로 저장
            chattingRoomService.save(chattingRoom);  // DB에 저장
            logger.info("caller_prediction 저장 후에 화긴!!!!!!!!!! basicActivityAnalysis: " + chattingRoom.getBasicActivityAnalysis());

            logger.error("Page10Controller /caller-prediction-exante Success 발신자 예측 사전준비 완료");
            return ResponseEntity.ok("Success");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("발신자예측 사전준비에서 에러남 ChattingRoom not found");
        }
    }

    // 파일에서 상위 10문장과 하위 10문장을 추출하는 메서드
    private String extractTopAndBottomMessages(InputStream inputStream) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        List<String> lines = new ArrayList<>();
        String line;

        // 파일의 모든 라인을 읽어 저장
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }

        int lineCount = lines.size();
        int topLinesCount = Math.min(10, lineCount);
        int bottomLinesCount = Math.min(10, lineCount - topLinesCount);

        // 상위 10문장과 하위 10문장 추출
        List<String> topLines = lines.subList(0, topLinesCount);
        List<String> bottomLines = lines.subList(lineCount - bottomLinesCount, lineCount);

        // 두 리스트를 합쳐 하나의 메시지로 반환
        List<String> selectedLines = new ArrayList<>(topLines);
        selectedLines.addAll(bottomLines);
        return String.join("\n", selectedLines);  // 각 문장을 줄바꿈으로 구분하여 반환
    }
}