package com.tuanwei.eyes.controller;

import com.tuanwei.eyes.entity.Users;
import com.tuanwei.eyes.repository.UsersRepository;
import com.tuanwei.eyes.service.UsersService;
import com.tuanwei.eyes.util.exception.EyeException;
import com.tuanwei.eyes.util.message.ErrorCodeAndMsg;
import com.tuanwei.eyes.util.message.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Transactional
@Slf4j
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private UsersService usersService;

    @GetMapping("/findAll")
    public List<Users> findAll(){
        return usersRepository.findAll();
    }

    @RequestMapping("/findById")
    public Response findById(@RequestParam Integer id){return new Response(usersRepository.findById(id).get());}

    @PostMapping("/login")
    public Response login(@RequestParam String email,@RequestParam String password){
        try {
            Users users = usersService.getUser(email,password);
            if (users == null) {
                throw new EyeException(ErrorCodeAndMsg.EmailAndPassword_Error);//账号密码查询不到
            }
            return new Response(users);
        }catch (Exception e){
            if(e instanceof EyeException){
                throw e;
            }else{
                log.error("login error:",e);
                throw new EyeException(ErrorCodeAndMsg.Network_error);
            }
        }
    }

    @PostMapping("/register")
    public Response register(@RequestBody Users users){
        try {
            boolean exist = usersService.isExist(users.getEmail());
            if(exist){
                throw new EyeException(ErrorCodeAndMsg.User_exist);
            }else {
                usersRepository.save(users);
                return new Response(users);


            }
        }catch (Exception e){
            if(e instanceof EyeException){
                throw e;
            }else{
                log.error("register error:",e);
                throw new EyeException(ErrorCodeAndMsg.Network_error);
            }
        }

    }

}
