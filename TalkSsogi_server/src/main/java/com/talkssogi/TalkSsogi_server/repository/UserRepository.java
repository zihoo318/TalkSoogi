package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUserId(String userId);

    void delete(User user);

    List<User> findAll();
}