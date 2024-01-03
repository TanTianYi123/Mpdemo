package com.example.mpdemo2.user.controller;/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/27 0027 14:23
 */

import com.example.mpdemo2.user.domain.User;
import com.example.mpdemo2.user.mapper.UserMapper;
import com.example.mpdemo2.user.service.UserService;
import com.example.mpdemo2.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/27 0027 14:23
 */
@Controller
@Slf4j
public class UserController {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    /*
     * 用户-密码登录
     */
    @RequestMapping("/login")
    public String Login(User userEntity, HttpServletRequest req, HttpServletResponse response) {

        String login = userService.login(userEntity, req, response);
        System.out.println(login);
        if (login.equals(Constants.SUCCESSCODE)) {
            // 登录成功
            log.info("用户" + userEntity.getUserName() + "登录成功");
            //判断权限
            String userName = userEntity.getUserName();
            User user;
            if(!userMapper.selectAllByUserName(userEntity.getUserName()).isEmpty()){
                user = userMapper.selectAllByUserName(userEntity.getUserName()).get(0);
                req.getSession().setAttribute("iconPath",user.getIcon());
                req.getSession().setAttribute("userName",user.getUserName());
                req.getSession().setMaxInactiveInterval(0);
                return "index";
            }

            return Constants.LOGIN;// 用户名或密码错误

        } else if (login.equals(Constants.ERROR)) {
            // 登录失败
            req.setAttribute(Constants.INFORMATION, Constants.LOGINERROE);
            log.error("用户" + userEntity.getUserName() + "登录失败");
            // 返回登录页面，缓存刚才登录的账户
            req.setAttribute(Constants.USERNAME, userEntity.getUserName());
            return Constants.LOGIN;// 用户名或密码错误
        } else {
            // 请先注册
            log.error("账号不存在，请先注册");
            req.setAttribute(Constants.INFORMATION, Constants.SIGNFIRST);
            return Constants.LOGIN;// 需要注册
        }
    }

}
