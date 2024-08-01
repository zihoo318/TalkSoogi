package com.talkssogi.TalkSsogi_server.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

public class PythonResultProcessor {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Map<String, Integer>> extractRankingResults(String jsonString) throws IOException {
        // JSON 문자열을 JsonNode로 변환
        JsonNode rootNode = objectMapper.readTree(jsonString);

        // 필요한 부분만 추출
        JsonNode rankingResultsNode = rootNode;

        // Map으로 변환
        return objectMapper.convertValue(rankingResultsNode, new TypeReference<Map<String, Map<String, Integer>>>() {});
    }
}
