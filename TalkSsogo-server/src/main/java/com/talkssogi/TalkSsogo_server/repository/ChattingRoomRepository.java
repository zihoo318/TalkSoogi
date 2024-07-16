package com.talkssogi.TalkSsogo_server.repository;

import com.talkssogi.TalkSsogo_server.domain.ChattingRoom;

import java.util.List;

public interface ChattingRoomRepository {
    ChattingRoom findByFilePath(String filePath);

    void save(ChattingRoom chattingRoom);

    void delete(ChattingRoom chattingRoom);

    List<ChattingRoom> findAll();
}