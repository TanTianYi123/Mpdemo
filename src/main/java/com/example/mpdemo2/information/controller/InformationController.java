package com.example.mpdemo2.information.controller;

import com.example.mpdemo2.information.domain.Information;
import com.example.mpdemo2.information.mapper.InformationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
@Slf4j
@Controller
public class InformationController {
    @Autowired
    InformationMapper informationMapper;

    @RequestMapping("/index")
    public String indexWeb(HttpServletRequest request){
        List<Information> informations = informationMapper.selectAllByIdEquals(1);
        Information information = informations.get(0);
        request.setAttribute("information", information);
        HttpSession session = request.getSession();
        session.setAttribute("company",information.getCompany());
        log.info("进入首页成功！");
        return "index";
    }
}
