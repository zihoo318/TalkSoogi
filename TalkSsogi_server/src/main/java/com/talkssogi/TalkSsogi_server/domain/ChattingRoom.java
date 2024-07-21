package com.talkssogi.TalkSsogi_server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "chattingroom")
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //기본 키 값을 자동으로 증가
    private Integer crNum;

    private String filePath;
    private int headcount;

    @ManyToOne //여러 ChattingRoom이 하나의 User와 관계
    @JoinColumn(name = "user_id") //ChattingRoom 테이블에서 외래 키로 사용될 컬럼의 이름이 user_id
    private User user;

    @OneToOne(mappedBy = "chattingRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
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