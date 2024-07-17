package com.talkssogi.TalkSsogi_server.repository;


import com.talkssogi.TalkSsogi_server.domain.ChattingRoom;

import java.util.ArrayList;
import java.util.List;

//임시 구현체
public class ChattingRoomRepositoryImpl implements ChattingRoomRepository {

    private List<ChattingRoom> chattingRoomList = new ArrayList<>();

    @Override
    public ChattingRoom findByFilePath(String filePath) {
        for (ChattingRoom room : chattingRoomList) {
            if (room.getFilePath().equals(filePath)) {
                return room;
            }
        }
        return null;
    }

    @Override
    public void save(ChattingRoom chattingRoom) {
        chattingRoomList.add(chattingRoom);
    }

    @Override
    public void delete(ChattingRoom chattingRoom) {
        chattingRoomList.remove(chattingRoom);
    }

    @Override
    public List<ChattingRoom> findAll() {
        return chattingRoomList;
    }
}