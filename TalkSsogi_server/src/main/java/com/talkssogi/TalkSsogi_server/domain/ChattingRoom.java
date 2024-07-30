package com.talkssogi.TalkSsogi_server.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "chattingroom")
public class ChattingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer crNum;

    private String filePath;
    private int headcount;

    @ManyToOne //여러 ChattingRoom이 하나의 User와 관계
    @JoinColumn(name = "user_id") //ChattingRoom 테이블에서 외래 키로 사용될 컬럼의 이름이 userId
    private User user;

    @OneToOne(mappedBy = "chattingRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    //AnalysisResult 엔티티에 chattingRoom이라는 필드가 있으며, 이 필드가 관계의 주인임 (이 테이블에서는 관계만 읽음 필드로 저장x)
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