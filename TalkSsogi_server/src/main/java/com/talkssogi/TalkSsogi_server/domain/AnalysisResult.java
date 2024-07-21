package com.talkssogi.TalkSsogi_server.domain;

import com.talkssogi.TalkSsogi_server.Converter.MapStringListConverter;
import com.talkssogi.TalkSsogi_server.Converter.StringListConverter;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
public class AnalysisResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Id
    private String chatroomName;

    @OneToOne //일대일
    @JoinColumn(name = "cr_num")
    private ChattingRoom chattingRoom;

    @Convert(converter = StringListConverter.class)
    private List<String> memberNames;

    @Convert(converter = MapStringListConverter.class)
    private Map<String, List<String>> basicActivityAnalysis;

    private String activityAnalysisImageUrl;
    private String wordCloudImageUrl;

    @Convert(converter = MapStringListConverter.class)
    private Map<String, List<String>> basicRankingResults;

    @Convert(converter = MapStringListConverter.class)
    private Map<String, List<String>> searchRankingResults;


    // for chatroomName
    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    // for memberNames
    public List<String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(List<String> memberNames) {
        this.memberNames = memberNames;
    }

    // for basicActivityAnalysis
    public Map<String, List<String>> getBasicActivityAnalysis() {
        return basicActivityAnalysis;
    }

    public void setBasicActivityAnalysis(Map<String, List<String>> basicActivityAnalysis) {
        this.basicActivityAnalysis = basicActivityAnalysis;
    }

    // for activityAnalysisImageUrl
    public String getActivityAnalysisImageUrl() {
        return activityAnalysisImageUrl;
    }

    public void setActivityAnalysisImageUrl(String activityAnalysisImageUrl) {
        this.activityAnalysisImageUrl = activityAnalysisImageUrl;
    }

    // for wordCloudImageUrl
    public String getWordCloudImageUrl() {
        return wordCloudImageUrl;
    }

    public void setWordCloudImageUrl(String wordCloudImageUrl) {
        this.wordCloudImageUrl = wordCloudImageUrl;
    }

    // for basicRankingResults
    public Map<String, List<String>> getBasicRankingResults() {
        return basicRankingResults;
    }

    public void setBasicRankingResults(Map<String, List<String>> basicRankingResults) {
        this.basicRankingResults = basicRankingResults;
    }

    // for searchRankingResults
    public Map<String, List<String>> getSearchRankingResults() {
        return searchRankingResults;
    }

    public void setSearchRankingResults(Map<String, List<String>> searchRankingResults) {
        this.searchRankingResults = searchRankingResults;
    }
}
