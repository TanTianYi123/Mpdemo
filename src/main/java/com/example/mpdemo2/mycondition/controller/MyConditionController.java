package com.example.mpdemo2.mycondition.controller;

import com.example.mpdemo2.mycondition.domain.MyCondition;
import com.example.mpdemo2.mycondition.mapper.MyConditionMapper;
import com.example.mpdemo2.util.Constants;
import lombok.extern.slf4j.Slf4j;
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

    @RequestMapping("/myConditionMonitor")
    public String myConditionMonitorWeb (HttpServletRequest request, HttpServletResponse rep, String key) throws IOException {
        List<MyCondition> myConditions = myConditionMapper.selectList(null);
        if(myConditions.isEmpty()){
            request.setAttribute(Constants.MSG, "暂无数据");
        }else {
            request.setAttribute("allMyConditionMonitors", myConditions);
        }
        log.info("进入工况监测页面成功！");
        return "myConditionMonitor";
    }
}
