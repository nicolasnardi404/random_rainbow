package com.randomrainbow.springboot.demosecurity.dao;

import com.randomrainbow.springboot.demosecurity.entity.User;

public interface UserDao {

    User findByUserName(String userName);

    void save(User theUser);

}
