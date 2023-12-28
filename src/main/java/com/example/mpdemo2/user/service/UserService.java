package com.example.mpdemo2.user.service;

import com.example.mpdemo2.user.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2023-12-27 14:12:04
*/
public interface UserService extends IService<User> {
    public String login(User userEntity, HttpServletRequest request, HttpServletResponse response);
}
