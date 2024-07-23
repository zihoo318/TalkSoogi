package com.talkssogi.TalkSsogi_server.domain;

import com.talkssogi.TalkSsogi_server.Converter.MapStringListConverter;
import com.talkssogi.TalkSsogi_server.Converter.StringListConverter;
import jakarta.persistence.*;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "analysisresult")
public class AnalysisResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne // 일대일 관계 설정
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @Column(name = "chatroom_name", length = 255)
    private String chatroomName;

    @OneToOne
    @JoinColumn(name = "cr_num", referencedColumnName = "crNum")
    private ChattingRoom chattingRoom;

    @Convert(converter = StringListConverter.class)
    private List<String> memberNames;

    @Convert(converter = MapStringListConverter.class)
    private Map<String, List<String>> basicActivityAnalysis;

    @Column(name = "activity_analysis_image_url", length = 255)
    private String activityAnalysisImageUrl;

    @Column(name = "word_cloud_image_url", length = 255)
    private String wordCloudImageUrl;

    @Convert(converter = MapStringListConverter.class)
    private Map<String, List<String>> basicRankingResults;

    @Convert(converter = MapStringListConverter.class)
    private Map<String, List<String>> searchRankingResults;

    // 기본 생성자
    public AnalysisResult() {
    }

    // 명시적 생성자
    public AnalysisResult(User user, String chatroomName, ChattingRoom chattingRoom, List<String> memberNames,
                          Map<String, List<String>> basicActivityAnalysis, String activityAnalysisImageUrl,
                          String wordCloudImageUrl, Map<String, List<String>> basicRankingResults,
                          Map<String, List<String>> searchRankingResults) {
        this.user = user;
        this.chatroomName = chatroomName;
        this.chattingRoom = chattingRoom;
        this.memberNames = memberNames;
        this.basicActivityAnalysis = basicActivityAnalysis;
        this.activityAnalysisImageUrl = activityAnalysisImageUrl;
        this.wordCloudImageUrl = wordCloudImageUrl;
        this.basicRankingResults = basicRankingResults;
        this.searchRankingResults = searchRankingResults;
    }

    // Getter 및 Setter
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getChatroomName() {
        return chatroomName;
    }

    public void setChatroomName(String chatroomName) {
        this.chatroomName = chatroomName;
    }

    public ChattingRoom getChattingRoom() {
        return chattingRoom;
    }

    public void setChattingRoom(ChattingRoom chattingRoom) {
        this.chattingRoom = chattingRoom;
    }

    public List<String> getMemberNames() {
        return memberNames;
    }

    public void setMemberNames(List<String> memberNames) {
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
