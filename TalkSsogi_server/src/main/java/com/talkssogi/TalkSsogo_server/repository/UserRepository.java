package com.talkssogi.TalkSsogo_server.repository;

import com.talkssogi.TalkSsogo_server.domain.User;

import java.util.List;

public interface UserRepository {
    User findByUserId(String userId);

    void save(User user);

    void delete(User user);

    List<User> findAll();
}