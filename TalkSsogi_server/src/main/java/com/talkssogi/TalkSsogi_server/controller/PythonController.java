package com.talkssogi.TalkSsogi_server.controller;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import com.talkssogi.TalkSsogi_server.processor.PythonResultProcessor;
import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import com.talkssogi.TalkSsogi_server.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/analysis")
public class PythonController {

    private static final Logger logger = LoggerFactory.getLogger(PythonController.class);

    private final ChattingRoomService chattingRoomService;
    private final UserService userService;
    //private final S3Uploader s3Uploader;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PythonResultProcessor pythonResultProcessor;  // 추가

    // Python 인터프리터와 스크립트의 경로를 상수로 선언
    private static final String PYTHON_INTERPRETER_PATH = "C:/Users/Master/AppData/Local/Programs/Python/Python312/python.exe";
    private static final String PYTHON_SCRIPT_basic_PATH = "C:/Users/Master/TalkSsogi_Workspace/basic-python.py";
    private static final String PYTHON_SCRIPT_PAGE9_PATH = "C:/Users/Master/TalkSsogi_Workspace/page9python.py";
    private static final String PYTHON_SCRIPT_PAGE6_PATH = "C:/Users/Master/TalkSsogi_Workspace/page6python.py";
    private static final String PYTHON_BASIC_RESULT_FILE_PATH = "C:/Users/Master/TalkSsogi_Workspace/"; // basic-python후에 생길 분석을 위한 파일들을 찾기 위한 경로
    private static final String PYTHON_newimage_PATH = "C:/Users/Master/TalkSsogi_Workspace/TalkSsogi_server"; // page9python후에 생길 이미지 파일 저장할 경로
    private final AmazonS3 s3Client = AmazonS3ClientBuilder.standard().build();
    private final String bucketName = "your-s3-bucket-name"; // S3 버킷 이름

    @Autowired
    public PythonController(ChattingRoomService chattingRoomService, UserService userService, PythonResultProcessor pythonResultProcessor) { //, S3Uploader s3Uploader 추가
        this.chattingRoomService = chattingRoomService;
        this.userService = userService;
        //this.s3Uploader = s3Uploader;
        this.pythonResultProcessor = pythonResultProcessor;  // 추가
    }

    // 처음 파일 업로드할 때 진행되는 기본 데이터 분석
    @GetMapping(value = "/basic-python", produces = "application/json; charset=utf8")
    public ResponseEntity<String> runBasicPythonAnalysis(@RequestParam(value = "crnum") int crnum) {
        try {
            // ChattingRoom을 찾아서 파일 경로를 가져온다
            ChattingRoom chattingRoom = chattingRoomService.findByCrNum(crnum);
            if (chattingRoom == null || chattingRoom.getCrNum() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ChattingRoom not found or invalid.");
            }
            String filePath = chattingRoom.getFilePath(); // 파일 경로 가져오기
            if (filePath == null || filePath.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File path is not set.");
            }
            int headcount = chattingRoom.getHeadcount(); // headcount 가져오기

            // 명령어 설정
            String command = String.format("%s %s %s", PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_basic_PATH, filePath);
            logger.info("제대로 파이썬 명령어를 사용하고 있는가???? Executing command: " + command);


            // ProcessBuilder를 사용하여 프로세스 생성
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");

            // 프로세스 시작
            Process process = processBuilder.start();

            // 프로세스의 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream())); // 표준 오류 스트림 읽기
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 표준 오류 스트림 읽기
            StringBuilder errorResult = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorResult.append(line).append("\n");
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            logger.error("파이썬 에러 메세지!!! Python script error output: " + errorResult.toString());
            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Script execution failed.");
            }

            // 결과 처리
            String[] resultLines = result.toString().split("\n");
            if (resultLines.length < 2) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected script output.");
            }


            // JSON 파일에서 결과를 읽어오기
            String jsonFilePath = PYTHON_BASIC_RESULT_FILE_PATH + "/ranking_results.json";
            File jsonFile = new File(jsonFilePath); // JSON 파일 경로 설정
            String jsonString = readFileToString(jsonFile);  // 파일을 문자열로 읽기

            // JSON 문자열을 Map으로 변환
            Map<String, Map<String, String>> rankingResultsMap = pythonResultProcessor.extractRankingResults(jsonString);

            // 분석 결과를 ChattingRoom 엔티티에 저장
            chattingRoom.setBasicRankingResults(rankingResultsMap);
            chattingRoomService.save(chattingRoom);

            // 분석 결과를 저장
            String chatroomName = resultLines[0];
            List<String> memberNames = List.of(resultLines[1].split(","));
            logger.info("제대로 파이썬 결과를 받았는가???? chatroomName: " + chatroomName);
            logger.info("제대로 파이썬 결과를 받았는가???? memberNames: " + memberNames);

            // ChattingRoom 업데이트
            chattingRoom.setChatroomName(chatroomName);
            chattingRoom.setMemberNames(memberNames);
            chattingRoomService.save(chattingRoom);

            logger.info("여기여여여여여여겨고ㅑ 분석한 결과부터 이상하게 저장되었는가 : {}", chattingRoom.getChatroomName());

            return ResponseEntity.ok("Success");
        } catch (EntityNotFoundException e) {
            logger.error("Entity not found: ", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }

    // 페이지6 워드클라우드 이미지 url전달
    @GetMapping("/wordCloudImageUrl/{crnum}/{userId}")
    public ResponseEntity<String> getWordCloudImageUrl(@PathVariable Integer crnum, @PathVariable String userId) {
        try {
            // userId 값 처리
            String searchWho = "전체".equals(userId) ? "group" : userId;

            // file_path 설정
            // S3에서 가져오기
            //filePath = downloadFileFromS3(String.format(crnum + (searchWho.equals("group") ? "/group.txt" : "/" + searchWho + "_personal.txt"));
            String filePath = PYTHON_newimage_PATH + crnum + (searchWho.equals("group") ? "/group.txt" : "/" + searchWho + "_personal.txt"); // 테스트용

            // 명령어 설정
            String command = String.format(
                    "%s %s %s",
                    PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_PAGE6_PATH, filePath
            );
            logger.info("제대로 파이썬 명령어를 사용하고 있는가???? Executing command: " + command);

            // ProcessBuilder를 사용하여 프로세스 생성
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");

            // 프로세스 시작
            Process process = processBuilder.start();

            // 프로세스의 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream())); // 표준 오류 스트림 읽기
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 표준 오류 스트림 읽기
            StringBuilder errorResult = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorResult.append(line).append("\n");
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            logger.error("Python script error output: " + errorResult.toString());

            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Script execution failed.");
            }

            // 결과 처리
            String resultOutput = result.toString().trim();
            if (resultOutput.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected script output.");
            }

            // 분석 결과 파일 경로 추출
            String resultFilePath = resultOutput;  // 결과 파일 경로는 Python 스크립트에서 생성된 파일 경로

            // 결과 이미지 URL 생성
            String wordCloudImageUrl = PYTHON_BASIC_RESULT_FILE_PATH + resultFilePath;
            logger.info("Generated result URL: " + wordCloudImageUrl);

            return new ResponseEntity<>(wordCloudImageUrl, HttpStatus.OK);

        } catch (IOException e) {
            logger.error("IO Exception occurred: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An IO exception occurred.");
        } catch (InterruptedException e) {
            logger.error("Process interrupted: " + e.getMessage(), e);
            Thread.currentThread().interrupt(); // Restore interrupted status
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Process was interrupted.");
        }
    }

    private String readFileToString(File file) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }


    // 5페이지에서 8로 갈 때 실행될 기본 제공 데이터
    @GetMapping("/basicActivityAnalysis") // 7,8의 기본 제공 데이터 생성
    public ResponseEntity<List<String>> commonPythonScript(@RequestParam(value = "crnum") int crnum) {
        try {
            // S3에서 파일 다운로드
            //String dailyFilePath = downloadFileFromS3(String.format("%d/group_daily_message_count.txt", crnum));
            //String hourlyFilePath = downloadFileFromS3(String.format("%d/group_daily_hourly_message_count.txt", crnum));
            String dailyFilePath = PYTHON_newimage_PATH + String.format("%d/group_daily_message_count.txt", crnum);
            String hourlyFilePath = PYTHON_newimage_PATH + String.format("%d/group_daily_hourly_message_count.txt", crnum);

            // 명령어 설정
            String command = String.format("%s %s %s %s", PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_basic_PATH, dailyFilePath, hourlyFilePath);
            logger.info("제대로 파이썬 명령어를 사용하고 있는가???? Executing command: " + command);

            // ProcessBuilder를 사용하여 프로세스 생성
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);

            // 프로세스 시작
            Process process = processBuilder.start();

            // 프로세스의 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            List<String> resultLines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                resultLines.add(line);
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();

            // 다운로드한 파일 삭제
            Files.deleteIfExists(Paths.get(dailyFilePath));
            Files.deleteIfExists(Paths.get(hourlyFilePath));

            if (exitCode == 0) {
                // 필요한 데이터 추출
                String maxDateAndCount = resultLines.size() > 0 ? resultLines.get(0) : "분석 실패";
                String zeroCountDates = resultLines.size() > 1 ? resultLines.get(1) : "분석 실패";
                String maxTimeSlotAndCount = resultLines.size() > 2 ? resultLines.get(2) : "분석 실패";

                // 리스트 생성 및 값 추가
                List<String> outputList = new ArrayList<>();
                outputList.add(maxDateAndCount);
                outputList.add(zeroCountDates);
                outputList.add(maxTimeSlotAndCount);

                return ResponseEntity.ok(outputList); // 리스트 반환
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        List.of("Script execution failed with exit code: " + exitCode)
                );
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    List.of("Error: " + e.getMessage())
            );
        }
    }

    // 날짜 형식을 바꿔주는 메서드
    private String convertDateFormat(String dateStr) throws ParseException {
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = originalFormat.parse(dateStr);
        return targetFormat.format(date);
    }

    // 페이지9에서 결과 이미지 생성 및 전달
    @PostMapping("/personalActivityAnalysisImage")
    public ResponseEntity<String> getActivityAnalysisImage(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam("searchWho") String searchWho,
            @RequestParam("resultsItem") String resultsItem,
            @RequestParam("crnum") Integer crnum) {
        try {
            // 날짜 형식 변환
            String startDateFormatted = convertDateFormat(startDate);
            String endDateFormatted = convertDateFormat(endDate);

            // SearchWho 값 처리
            String SearchWho = "전체".equals(searchWho) ? "group" : searchWho;

            // crnum을 가지고 s3에서 파일 찾고 가져오는 과정
            String filePath = null; // 검색할 파일
            // resultsItem에 따라 파일 경로를 설정
            switch (resultsItem) {
                case "보낸 메시지 수 그래프":
                    //filePath = downloadFileFromS3(String.format("%d/%s_daily_message_count.txt", crnum, SearchWho));
                    filePath = PYTHON_newimage_PATH + String.format("%d/%s_daily_message_count.txt", crnum, SearchWho); //테스트용
                    break;
                case "활발한 시간대 그래프":
                    //filePath = downloadFileFromS3(String.format("%d/%s_daily_hourly_message_count.txt", crnum, SearchWho));
                    filePath = PYTHON_newimage_PATH + String.format("%d/%s_daily_hourly_message_count.txt", crnum, SearchWho); //테스트용
                    break;
                case "대화를 보내지 않은 날짜":
                    //filePath = downloadFileFromS3(String.format("%d/%s_daily_message_count.txt", crnum, SearchWho));
                    filePath = PYTHON_newimage_PATH + String.format("%d/%s_daily_message_count.txt", crnum, SearchWho); //테스트용
                default:
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("resultsItem이 이상함 Unknown resultsItem: " + resultsItem);
            }

            // 명령어 설정
            String command = String.format(
                    "%s %s \"%s\" \"%s\" \"%s\" \"%s\" %s",
                    PYTHON_INTERPRETER_PATH, PYTHON_SCRIPT_PAGE9_PATH, startDateFormatted, endDateFormatted, SearchWho, resultsItem, filePath
            );
            logger.info("제대로 파이썬 명령어를 사용하고 있는가???? Executing command: " + command);
            // ProcessBuilder를 사용하여 프로세스 생성
            ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
            processBuilder.redirectErrorStream(true);
            processBuilder.environment().put("PYTHONIOENCODING", "UTF-8");

            // 프로세스 시작
            Process process = processBuilder.start();

            // 프로세스의 출력 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream())); // 표준 오류 스트림 읽기
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            // 표준 오류 스트림 읽기
            StringBuilder errorResult = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorResult.append(line).append("\n");
            }

            // 프로세스 종료 대기
            int exitCode = process.waitFor();
            logger.error("Python script error output: " + errorResult.toString());

            if (exitCode != 0) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Script execution failed.");
            }

            // 결과 처리
            String resultOutput = result.toString().trim();
            if (resultOutput.isEmpty()) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected script output.");
            }

            // 분석 결과 파일 경로 추출
            String resultFilePath = resultOutput;  // 결과 파일 경로는 Python 스크립트에서 생성된 파일 경로

            // 결과 이미지 URL 생성
            String resultUrl = PYTHON_BASIC_RESULT_FILE_PATH + resultFilePath;
            logger.info("Generated result URL: " + resultUrl);

            return ResponseEntity.ok(resultUrl);
        } catch (Exception e) {
            logger.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error: " + e.getMessage());
        }
    }
}