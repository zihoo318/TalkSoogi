package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, String> {
    ChattingRoom findByFilePath(String filePath);

    void delete(ChattingRoom chattingRoom);

    List<ChattingRoom> findAll();
}