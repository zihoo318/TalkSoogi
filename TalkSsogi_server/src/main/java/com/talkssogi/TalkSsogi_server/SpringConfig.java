//package com.talkssogi.TalkSsogi_server;
//
//import com.talkssogi.TalkSsogi_server.repository.*;
//import com.talkssogi.TalkSsogi_server.service.*;
//import jakarta.persistence.EntityManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class SpringConfig {
//
//    private final EntityManager em;
//
//    public SpringConfig(EntityManager em) {
//        this.em = em;
//    }
//
//    @Bean
//    public UserService userService() {
//        return new UserService();
//    }
//
//    @Bean
//    public UserRepository userRepository() {
//        return new JPAUserRepository(em);
//    }
//
//    @Bean
//    public ChattingRoomService chattingRoomService() {
//        return new ChattingRoomService(chattingRoomRepository(), userRepository());
//    }
//
//    @Bean
//    public ChattingRoomRepository chattingRoomRepository() {
//        return new JPAChattingRepository(em);
//    }
//
//    @Bean
//    public AnalysisResultService analysisResultService() {
//        return new AnalysisResultService();
//    }
//
//    @Bean
//    public AnalysisResultRepository analysisResultRepository() {
//        return new JPAAnalysisResultRepository(em);
//    }
//}
