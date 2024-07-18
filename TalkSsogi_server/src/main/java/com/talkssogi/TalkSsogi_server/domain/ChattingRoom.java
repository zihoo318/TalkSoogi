package com.talkssogi.TalkSsogi_server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "chattingroom")
public class ChattingRoom {
    @Id
    private String filePath; // filePath를 기본키로 설정

    private int headcount;
    private AnalysisResult analysisResult;

    public ChattingRoom(String filePath, int headcount) {
        this.filePath = filePath;
        this.headcount = headcount;
        this.analysisResult = null; // AnalysisResult는 optional로 설정
    }

    // Getters and setters
    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }

    public AnalysisResult getAnalysisResult() {
        return analysisResult;
    }

    public void setAnalysisResult(AnalysisResult analysisResult) {
        this.analysisResult = analysisResult;
    }
}