package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;

public interface AnalysisResultRepository {
    AnalysisResult findByActivityAnalysisImageUrl(String activityAnalysisImageUrl);

    AnalysisResult findByWordCloudImageUrl(String wordCloudImageUrl);

    void save(AnalysisResult analysisResult);

    void delete(AnalysisResult analysisResult);

    AnalysisResult findByUserId(String userId);
}

