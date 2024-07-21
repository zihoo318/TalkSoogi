package com.talkssogi.TalkSsogi_server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "chattingroom")
public class ChattingRoom {
    @Id
    private Integer crNum;

    @ManyToOne //여러 ChattingRoom이 하나의 User와 관계
    @JoinColumn(name = "user_id") //ChattingRoom 테이블에서 외래 키로 사용될 컬럼의 이름이 user_id
    private User user;

    private String filePath;
    private int headcount;


    @OneToOne(mappedBy = "chattingRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AnalysisResult analysisResult;

    // Getters and setters
    public Integer getCrNum() { return crNum;  }

    public void setCrNum(Integer crNum) { this.crNum = crNum;  }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getHeadcount() { return headcount; }

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