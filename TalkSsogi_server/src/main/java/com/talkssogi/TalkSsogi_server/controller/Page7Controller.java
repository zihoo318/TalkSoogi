package com.talkssogi.TalkSsogi_server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
page7 서버에서 가져올 데이터
- 2차원 배열의 랭킹 결과(1-9)
- 검색할 데이터 - search_page7
- 랭킹 결과(검색된 단어)
=>page7Service에서 파이썬 코드를 실행해서 생성된 AnalysisResult의 basicRankingResults랑 searchRankingResults를 전달하도록 할건데
아직 파이썬 코드를 못 만들어서 임시로 각 객체의 변수에 값이 있다고 치고 전달하도록 서비스파일이랑 컨트롤러 파일을 만들기
->서비스에서 안드로 보낼 데이터를 만들고(=파이썬 돌렸다 치고 임시 대이터 만드는 과정)
->컨트롤러에서 api만들어서 매핑(=basicRankingResults랑 searchRankingResults 따로 만들어야함)
=>안드로이드 스튜디오에서 만든 코드 다시 보고 어떤 형태로 데이터를 넘겨줘야하는지 고려해서 api만들기
 */

@RestController
@RequestMapping("/api/rankings")
public class Page7Controller {

    @GetMapping("/basicRankingResults")
    public Map<String, List<String>> getBasicRankingResults() {
        // 여기에 기본 제공 랭킹 결과를 생성하는 로직을 추가합니다.
        Map<String, List<String>> basicRankingResults = new HashMap<>();
        basicRankingResults.put("주제1", List.of("조", "유", "진"));
        basicRankingResults.put("주제2", List.of("강", "지", "후"));
        basicRankingResults.put("주제3", List.of("정", "가", "을"));
        basicRankingResults.put("주제4", List.of("강", "윤", "지"));
        basicRankingResults.put("주제5", List.of("조", "마", "오"));
        basicRankingResults.put("주제6", List.of("홍", "길", "동"));
        basicRankingResults.put("주제7", List.of("김", "한", "성"));
        basicRankingResults.put("주제8", List.of("차", "은", "우"));
        basicRankingResults.put("주제9", List.of("이름A", "이름B", "이름C"));
        return basicRankingResults;
    }

    @GetMapping("/searchRankingResults")
    public Map<String, List<String>> getSearchRankingResults() {
        // 여기에 검색 랭킹 결과를 생성하는 로직을 추가합니다.
        Map<String, List<String>> searchRankingResults = new HashMap<>();
        searchRankingResults.put("검색어1", List.of("이름1", "이름2", "이름3"));
        searchRankingResults.put("검색어2", List.of("이름A", "이름B", "이름C"));
        return searchRankingResults;
    }
}