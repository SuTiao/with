package com.sutiao.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutiao.app.constant.UserConstant;
import com.sutiao.app.service.UserService;
import com.sutiao.app.pojo.User;
import com.sutiao.app.mapper.UserMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author 田志隐
* @description 针对表【user(user)】的数据库操作Service实现
* @createDate 2023-10-16 18:12:06
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        if(StringUtils.isAnyBlank(userAccount,userPassword,checkPassword)){
            return -1;
        }
        if(userAccount.length() < 4){
            return -1;
        }
        if(userPassword.length() < 8 || checkPassword.length() < 8){
            return -1;
        }

        String validPattern = "\"[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]\"";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return -1;
        }
        if(!userPassword.equals(checkPassword)){
            return -1;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if(count > 0){
            return -1;
        }

        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());

        User user = new User();
        user.setAccount(userAccount);
        user.setPassword(encryptPassword);
        user.setUsername("user_" + UUID.randomUUID());
        boolean saveResult = this.save(user);

        if(!saveResult)
            return -1;

        return user.getId();

    }

    @Override
    public User userLogin(String userAccount, String Password, HttpServletRequest request) {

        if(StringUtils.isAnyBlank(userAccount,Password)){
            return null;
        }
        if(userAccount.length() < 4){
            return null;
        }
        if(Password.length() < 8){
            return null;
        }

        String validPattern = "\"[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]\"";
        Matcher matcher = Pattern.compile(validPattern).matcher(userAccount);
        if(matcher.find()){
            return null;
        }



        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + Password).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("password",encryptPassword);
        queryWrapper.eq("account",userAccount);
        User uu = userMapper.selectOne(queryWrapper);
        if(uu == null){
            log.info("user login failed, user Account cannot match.");
            return null;
        }

        User u = getSafetyUser(uu);



        request.getSession().setAttribute(UserConstant.User_LOGIN_STATE,u);


        return u;
    }

    @Override
    public User getSafetyUser(User user){
        User u = new User();
        u.setId(user.getId());
        u.setUsername(user.getUsername());
        u.setAccount(user.getAccount());
        u.setAvatarUrl(user.getAvatarUrl());
        u.setUserStatus(user.getUserStatus());
        u.setCreateTime(user.getCreateTime());
        u.setUpdateTime(user.getUpdateTime());
        return u;
    }

}




