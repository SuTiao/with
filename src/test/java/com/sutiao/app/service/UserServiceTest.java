package com.sutiao.app.service;

import com.sutiao.app.pojo.User;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Resource
    private UserService userService;
    @Test
    public void test(){
        User u = new User();
        u.setId(15L);
        u.setUsername("a");
        u.setAccount("a");
        u.setPassword("a");
        u.setAvatarUrl("a");
        u.setUserStatus(1);
        u.setCreateTime(new Date());
        u.setUpdateTime(new Date());


        userService.save(u);
    }

    @Test
    void userRegister() {
        String a = "aaaaa";
        String b = "bbbbbbbb";
        String c = "bbbbbbbb";
        System.out.println(userService.userRegister(a,b,c));

    }
}