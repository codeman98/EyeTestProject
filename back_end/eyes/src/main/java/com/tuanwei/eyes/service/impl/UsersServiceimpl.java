package com.tuanwei.eyes.service.impl;

import com.tuanwei.eyes.entity.Users;
import com.tuanwei.eyes.repository.UsersRepository;
import com.tuanwei.eyes.service.UsersService;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersServiceimpl implements UsersService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public void addUser(@NonNull Users user) {
        usersRepository.save(user);
    }

    @Override
    public void deleteUser(int id) {
        usersRepository.deleteById(id);
    }

    @Override
    public void updateUser(@NonNull Users user) {
        usersRepository.updateUser(user.getEmail(),user.getPassword());
    }

    @Override
    public Users getUser(int id) {
        return usersRepository.findById(id).get();
    }

    @Override
    public List<Users> list() {
        return usersRepository.findAll();
    }

    @Override
    public boolean isExist(String email) {
        Users users = new Users();
        users.setEmail(email);
        Example<Users> example = Example.of(users);
        //判断是否存在该用户
        Users re_users = usersRepository.findOne(example).isPresent()?usersRepository.findOne(example).get():null;
        if(re_users==null){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public Users getUser(String email, String password) {
        Users users = new Users();
        users.setEmail(email);
        users.setPassword(password);
        Example<Users> example = Example.of(users);
        //判断是否存在该用户和密码
        Users re_users = usersRepository.findOne(example).isPresent()?usersRepository.findOne(example).get():null;
        return re_users;
    }
}
