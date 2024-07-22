package com.talkssogi.TalkSsogi_server.service;

import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
import com.talkssogi.TalkSsogi_server.repository.AnalysisResultRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AnalysisResultService {
    @Autowired
    private AnalysisResultRepository analysisResultRepository;

    public void addAnalysisResult(AnalysisResult result) {
        analysisResultRepository.save(result);
    }

    public AnalysisResult findAnalysisResultByActivityAnalysisImageUrl(String activityAnalysisImageUrl) {
        return analysisResultRepository.findByActivityAnalysisImageUrl(activityAnalysisImageUrl);
    }

    public AnalysisResult findAnalysisResultByWordCloudImageUrl(String wordCloudImageUrl) {
        return analysisResultRepository.findByWordCloudImageUrl(wordCloudImageUrl);
    }

    public String findWordCloudImageUrlByChatRoomIdAndUserId(Integer chatRoomId, String userId) {
        AnalysisResult analysisResult = analysisResultRepository.findByChattingRoom_CrNumAndChattingRoom_User_UserId(chatRoomId, userId);
        if (analysisResult != null) {
            return analysisResult.getWordCloudImageUrl();
        }
        return null;
    }
}
