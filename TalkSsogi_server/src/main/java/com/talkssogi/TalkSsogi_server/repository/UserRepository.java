package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.User;

import java.util.List;

public interface UserRepository {
    User findByUserId(String userId);

    void save(User user);

    void delete(User user);

    List<User> findAll();
}