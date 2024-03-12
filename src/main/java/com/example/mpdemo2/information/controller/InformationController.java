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
    @RequestMapping("/updateInformation")
    public String updateInformationShow(HttpServletRequest request){
        List<Information> informations = informationMapper.selectAllByIdEquals(1);
        Information information = informations.get(0);
        request.setAttribute("information", information);
        log.info("进入企业信息修改页面成功！");
        return "updateInformation";
    }
    @RequestMapping("/updataCompanyInformation")
    public String updataInformationEntity(HttpServletRequest request, Information information) {
        try{
            informationMapper.updateById(information);
        }catch (Exception e){
            log.error("企业信息修改失败！" +e);
            return updateInformationShow(request);
        }
        request.setAttribute("msg", "企业信息修改成功！");
        log.info("企业信息修改成功！");
        return indexWeb(request);
    }

}
