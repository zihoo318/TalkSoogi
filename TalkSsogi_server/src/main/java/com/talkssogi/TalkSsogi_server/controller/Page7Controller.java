package com.talkssogi.TalkSsogi_server.controller;

import com.talkssogi.TalkSsogi_server.service.ChattingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rankings")
public class Page7Controller {

    private static final Logger logger = LoggerFactory.getLogger(Page7Controller.class);

    @Autowired
    private ChattingRoomService chattingRoomService;

    @GetMapping("/basicRankingResults")
    public Map<String, Map<String, String>> getBasicRankingResults(@RequestParam Integer crnum) {
        // 서비스에서 기본 제공 랭킹 결과를 가져오는 로직
        Map<String, Map<String, String>> basicRankingResults = chattingRoomService.getBasicRankingResults(crnum);

        // 결과값 로그 출력
        logger.info("Basic Ranking Results for crnum {}: {}", crnum, basicRankingResults);

        return basicRankingResults;
    }

    @GetMapping("/searchRankingResults")
    public Map<String, List<String>> getSearchRankingResults(@RequestParam Integer crnum, @RequestParam String keyword) {
        // 기본 제공 랭킹 결과를 생성
        Map<String, List<String>> searchRankingResults = new HashMap<>();
        searchRankingResults.put("qqq", List.of("이름1", "이름2", "이름3"));
        searchRankingResults.put("www", List.of("이름4", "이름5", "이름6"));
        searchRankingResults.put("eee", List.of("이름7", "이름8", "이름9"));
        searchRankingResults.put("rrr", List.of("이름10", "이름11", "이름12"));
        searchRankingResults.put("ttt", List.of("이름13", "이름14", "이름15"));
        searchRankingResults.put("yyy", List.of("이름16", "이름17", "이름18"));
        // 검색어에 따라 결과를 필터링
        Map<String, List<String>> filteredResults = searchRankingResults.entrySet().stream()
                .filter(entry -> entry.getKey().equals(keyword))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        // 결과값 로그 출력
        logger.info("Search Ranking Results for crnum {} and keyword {}: {}", crnum, keyword, filteredResults);

        return filteredResults;
    }
}
