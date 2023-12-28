package com.example.mpdemo2.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.user.domain.User;
import com.example.mpdemo2.user.service.UserService;
import com.example.mpdemo2.user.mapper.UserMapper;
import com.example.mpdemo2.util.Constants;
import com.example.mpdemo2.util.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-12-27 14:12:04
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Autowired
    UserMapper mapper;
    public String login(User userEntity, HttpServletRequest request, HttpServletResponse response){
        String loginStr = userEntity.getUserName();
        String password = userEntity.getPassword();
        try{
            List<User> users = mapper.selectAllByUserName(loginStr);
            if(!users.isEmpty()){
                User myuser = users.get(0);
                String name = myuser.getUserName();
                String loginPw = MD5Util.MD5(name+password);
                if(loginPw.equals(myuser.getPassword())){
                    Cookie cookie = new Cookie(Constants.COOKIEHEAD, name);
                    cookie.setMaxAge(Constants.COOKIE_TTL); // 24h
                    response.addCookie(cookie);
                    log.info("登录成功");
                    return Constants.SUCCESSCODE;
                }else{
                    log.error("UserService/login, 登录失败，用户名或密码错误，账户：" + loginStr + " 密码：" + password);
                    return Constants.ERROR;
                }
            }
            else{
                return Constants.ERROR;
            }
        }catch(Exception e){
            log.error("UserService/login, 账号查询失败，请先注册或重新登录", e);
            return Constants.ERROR;
        }
    }
}




