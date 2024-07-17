package com.talkssogi.TalkSsogi_server.repository;

import com.talkssogi.TalkSsogi_server.domain.User;

import java.util.ArrayList;
import java.util.List;

//임시 구현체
public class UserRepositoryImpl implements UserRepository {

    private List<User> userList = new ArrayList<>();

    @Override
    public User findByUserId(String userId) {
        for (User user : userList) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void save(User user) {
        userList.add(user);
    }

    @Override
    public void delete(User user) {
        userList.remove(user);
    }

    @Override
    public List<User> findAll() {
        return userList;
    }
}