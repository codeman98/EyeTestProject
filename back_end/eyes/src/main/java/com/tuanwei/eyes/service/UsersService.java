package com.tuanwei.eyes.service;

import com.tuanwei.eyes.entity.Users;
import lombok.NonNull;

import java.util.List;

public interface UsersService {
    void addUser(@NonNull Users user);
    void deleteUser(int id);
    void updateUser(@NonNull Users user);
    Users getUser(int id);
    List list();
    boolean isExist(String email);

    Users getUser(String email,String password);
}
