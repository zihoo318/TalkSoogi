package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisResultRepository extends JpaRepository<AnalysisResult, String>{
    AnalysisResult findByActivityAnalysisImageUrl(String activityAnalysisImageUrl);

    AnalysisResult findByWordCloudImageUrl(String wordCloudImageUrl);

    void delete(AnalysisResult analysisResult);

    AnalysisResult findByUserId(String userId);
}

