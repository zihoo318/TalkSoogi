package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, String> {
    ChattingRoom findByFilePath(String filePath);
}