package com.talkssogi.TalkSsogo_server.repository;

import com.talkssogi.TalkSsogo_server.domain.AnalysisResult;

public interface AnalysisResultRepository {
    AnalysisResult findByActivityAnalysisImageUrl(String activityAnalysisImageUrl);

    AnalysisResult findByWordCloudImageUrl(String wordCloudImageUrl);

    void save(AnalysisResult analysisResult);

    void delete(AnalysisResult analysisResult);
}

