package com.example.mpdemo2.base;/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/27 0027 14:10
 */

import com.example.mpdemo2.user.domain.User;
import com.example.mpdemo2.user.mapper.UserMapper;
import com.example.mpdemo2.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/27 0027 14:10
 */
@Controller
@Slf4j
public class BaseController {
    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/")
    public String AIO(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Cookie[] cookies = req.getCookies();
        boolean flag = false;
        String userName = null;
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (ck.getName().equals(Constants.COOKIEHEAD)) {
                    flag = true;
                    userName = ck.getValue();
                    log.info("本次登录用户:{}", ck.getValue());
                    break;
                }
            }
        }
        if (!flag) {
            return Constants.LOGIN;
        }
        User userEntity = new User();
        req.getSession().setAttribute("iconPath",userEntity.getIcon());
        req.getSession().setAttribute("userName",userEntity.getUserName());

        req.getSession().setMaxInactiveInterval(0);
        log.info("用户：" + userEntity.getUserName() + "登陆系统");
        return Constants.LOGIN;
    }
}

