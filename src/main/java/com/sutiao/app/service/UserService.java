package com.sutiao.app.service;

import com.sutiao.app.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
* @author 田志隐
* @description 针对表【user(user)】的数据库操作Service
* @createDate 2023-10-16 18:12:06
*/
public interface UserService extends IService<User> {

    long userRegister(String userAccount,String userPassword,String checkPassword);

    User userLogin(String userAccount, String Password, HttpServletRequest request);

    User getSafetyUser(User user);
}
