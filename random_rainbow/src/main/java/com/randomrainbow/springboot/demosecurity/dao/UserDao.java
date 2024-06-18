 package com.randomrainbow.springboot.demosecurity.dao;

import java.util.Optional;

import com.randomrainbow.springboot.demosecurity.entity.User;

public interface UserDao {

    Optional<User> findByUserName(String userName);

    void save(User theUser);

}
