package com.sutiao.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sutiao.app.constant.UserConstant;
import com.sutiao.app.pojo.User;
import com.sutiao.app.pojo.userDTO;
import com.sutiao.app.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class userController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public Long userRegister(HttpServletRequest request){
        String userAccount = request.getParameter("userAccount");
        String userPassword = request.getParameter("userPassword");
        String checkPassword = request.getParameter("checkPassword");
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return null;
        }
        return userService.userRegister(userAccount, userPassword, checkPassword);
    }


    @PostMapping("/login")
    public User userLogin(HttpServletRequest request){
        if(request == null)
            return null;
        String userAccount = request.getParameter("userAccount");
        String userPassword = request.getParameter("userPassword");
        System.out.println(userAccount + userPassword);
        if(StringUtils.isAnyBlank(userAccount,userPassword)){
            return null;
        }
        return userService.userLogin(userAccount, userPassword,request);
    }

    @GetMapping("/search")
    public List<User> searchUsers(String username,HttpServletRequest request){
        if(!authorized(request)){
            return new ArrayList<>();
        }


        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(username)){
            queryWrapper.like("username",username);
        }
        List<User> list = userService.list(queryWrapper);

        return list.stream().peek(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
    }

    @GetMapping("/current")
    public userDTO getCurrentUser(HttpServletRequest request){
        Object s = request.getSession().getAttribute(UserConstant.User_LOGIN_STATE);
        User u = (User)s;
        if(u == null){
            return null;
        }
        Long userId = u.getId();

        //TODO 校验用户是否合法
        User uu = userService.getById(userId);
        if(uu == null){
            return null;
        }
        userDTO userDTO = new userDTO();
        userDTO.setId(uu.getId());
        userDTO.setUsername(uu.getUsername());
        userDTO.setAvatarUrl(uu.getAvatarUrl());
        userDTO.setAccount(uu.getAccount());
        userDTO.setRole(uu.getRole());
        return userDTO;
    }



    @DeleteMapping("/delete")
    public boolean deleteUsers(@RequestBody long id,HttpServletRequest request){

        if(id <= 0 || !authorized(request)){
            return false;
        }
        return userService.removeById(id);
    }

    private boolean authorized(HttpServletRequest request){
        Object s = request.getSession().getAttribute(UserConstant.User_LOGIN_STATE);
        User user = (User) s;
        if(user == null || user.getRole() != UserConstant.ADMINFLAG){
            return false;
        }
        return true;
    }

}
