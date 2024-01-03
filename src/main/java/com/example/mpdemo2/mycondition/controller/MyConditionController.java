package com.example.mpdemo2.mycondition.controller;

import cn.hutool.core.date.DateUtil;
import com.example.mpdemo2.mycondition.domain.MyCondition;
import com.example.mpdemo2.mycondition.mapper.MyConditionMapper;
import com.example.mpdemo2.mycondition.service.MyConditionService;
import com.example.mpdemo2.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 接受数采仪数据
 */
@Controller
@Slf4j
public class MyConditionController {
    @Autowired
    private MyConditionMapper myConditionMapper;
    @Autowired
    private MyConditionService myConditionService;

    @RequestMapping("/myConditionMonitor")
    public String myConditionMonitorWeb (HttpServletRequest request) {
        List<MyCondition> myConditions = myConditionMapper.selectList(null);
        if(myConditions.isEmpty()){
            request.setAttribute(Constants.MSG, "暂无数据");
        }else {
            request.setAttribute("allMyConditionMonitors", myConditions);
        }
        log.info("进入工况监测页面成功！");
        return "myConditionMonitor";
    }

    @RequestMapping("/returnConditiontabledata")
    public void returnConditiontabledata (HttpServletResponse response,String instrumentIdId, String date, String pageSize) throws IOException{
        PageInfo<MyCondition> myConditions = myConditionService.returnselecttabledata(instrumentIdId, date);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if(myConditions.getPages() == 0){
            response.getWriter().write("{\"msg\": \"暂无数据\"}");
        }else {
            // 使用Jackson的ObjectMapper来转换对象为JSON字符串
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(myConditions);
            response.getWriter().write(json);
            log.info("返回选择的工况数据！");
        }
    }
}
