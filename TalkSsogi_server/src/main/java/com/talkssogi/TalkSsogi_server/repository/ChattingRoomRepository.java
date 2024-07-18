package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;

import java.util.List;

public interface ChattingRoomRepository {
    ChattingRoom findByFilePath(String filePath);

    void save(ChattingRoom chattingRoom);

    void delete(ChattingRoom chattingRoom);

    List<ChattingRoom> findAll();
}