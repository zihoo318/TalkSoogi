package com.talkssogi.TalkSsogi_server.domain;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class AnalysisResult {
    private String chatroomName;
    private String[] memberNames;
    private Map<String, List<String>> basicActivityAnalysis;
    private String activityAnalysisImageUrl;
    private String wordCloudImageUrl;
    private Map<String, List<String>> basicRankingResults;
    private Map<String, List<String>> searchRankingResults;

    public String getChatroomName() { return chatroomName; }

    public void setChatroomName(String chatroomName) { this.chatroomName = chatroomName; }

    public String[] getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(String[] memberNames) {
        this.memberNames = memberNames;
    }

    public Map<String, List<String>> getBasicActivityAnalysis() {
        return basicActivityAnalysis;
    }

    public void setBasicActivityAnalysis(Map<String, List<String>> basicActivityAnalysis) {
        this.basicActivityAnalysis = basicActivityAnalysis;
    }

    public String getActivityAnalysisImageUrl() {
        return activityAnalysisImageUrl;
    }

    public void setActivityAnalysisImageUrl(String activityAnalysisImageUrl) {
        this.activityAnalysisImageUrl = activityAnalysisImageUrl;
    }

    public String getWordCloudImageUrl() {
        return wordCloudImageUrl;
    }

    public void setWordCloudImageUrl(String wordCloudImageUrl) {
        this.wordCloudImageUrl = wordCloudImageUrl;
    }

    public Map<String, List<String>> getBasicRankingResults() {
        return basicRankingResults;
    }

    public void setBasicRankingResults(Map<String, List<String>> basicRankingResults) {
        this.basicRankingResults = basicRankingResults;
    }

    public Map<String, List<String>> getSearchRankingResults() {
        return searchRankingResults;
    }

    public void setSearchRankingResults(Map<String, List<String>> searchRankingResults) {
        this.searchRankingResults = searchRankingResults;
    }
}
