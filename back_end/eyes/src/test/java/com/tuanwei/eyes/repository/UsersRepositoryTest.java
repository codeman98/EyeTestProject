package com.tuanwei.eyes.repository;

import com.tuanwei.eyes.entity.Users;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsersRepositoryTest {
    @Autowired
    private UsersRepository bookrepository;

    @Test
    void findAll(){
        System.out.println(bookrepository.findAll());
    }

    @Test
    void findByUsers(){
//        Users users = bookrepository.findByUsername("泽").get(0);
//        System.out.println(users);
        Users users = new Users();
        users.setUsername("泽");
        Example<Users> example = Example.of(users);
        System.out.println(bookrepository.findOne(example).get());
    }

    @Test
    void updateUsers(){
        Users users = new Users();
        users.setEmail("1234567891@qq.com");
        users.setPassword("12");
        bookrepository.updateUser(users.getEmail(),users.getPassword());
    }

    @Test
    void isExist(){
        Users users = new Users();
        users.setEmail("1234567891@qq.com");
        Example<Users> example = Example.of(users);
        if(bookrepository.findOne(example).get()==null){
            System.out.println("false");
        }else {
            System.out.println("true");
        }
    }
}