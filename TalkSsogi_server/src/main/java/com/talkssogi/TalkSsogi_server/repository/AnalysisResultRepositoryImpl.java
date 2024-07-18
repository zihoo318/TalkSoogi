//package com.talkssogi.TalkSsogi_server.repository;
//
//import com.talkssogi.TalkSsogi_server.domain.AnalysisResult;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
////임시 구현체
//public class AnalysisResultRepositoryImpl implements AnalysisResultRepository {
//
//    private List<AnalysisResult> analysisResultList = new ArrayList<>();
//
//    @Override
//    public AnalysisResult findByActivityAnalysisImageUrl(String activityAnalysisImageUrl) {
//        for (AnalysisResult result : analysisResultList) {
//            if (result.getActivityAnalysisImageUrl().equals(activityAnalysisImageUrl)) {
//                return result;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public AnalysisResult findByWordCloudImageUrl(String wordCloudImageUrl) {
//        for (AnalysisResult result : analysisResultList) {
//            if (result.getWordCloudImageUrl().equals(wordCloudImageUrl)) {
//                return result;
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public void save(AnalysisResult analysisResult) {
//        analysisResultList.add(analysisResult);
//    }
//
//    @Override
//    public void delete(AnalysisResult analysisResult) {
//        analysisResultList.remove(analysisResult);
//    }
//
//    @Override
//    public AnalysisResult findByUserId(String userId) {
//        return null;
//    }
//}