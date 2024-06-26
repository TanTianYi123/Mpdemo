package com.example.mpdemo2.mycondition.controller;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
        QueryWrapper<MyCondition> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", 0).select("DISTINCT instrument_id");
        List<MyCondition> myConditions = myConditionMapper.selectList(queryWrapper);
        if(myConditions.isEmpty()){
            request.setAttribute(Constants.MSG, "暂无数据");
        }else {
            request.setAttribute("allMyConditionMonitors", myConditions);
        }
        log.info("进入工况监测页面成功！");
        return "myConditionMonitor";
    }

    @RequestMapping("/returnConditiontabledata")
    public void returnConditiontabledata (HttpServletResponse response,HttpServletRequest request,String instrumentId, String date) throws IOException {
        List<MyCondition> allMyConditionMonitors = myConditionService.returnselecttabledata(instrumentId, date);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if(allMyConditionMonitors.isEmpty()){
            response.getWriter().write("{\"msg\": \"暂无数据\"}");
        }else {
            // 使用Jackson的ObjectMapper来转换对象为JSON字符串
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(allMyConditionMonitors);
            response.getWriter().write(json);
            log.info("返回选择的工况数据！");
        }

    }
    @ResponseBody
    @RequestMapping("/test")
    public String test(){
        return "fuck";
    }
    @RequestMapping("/instrumentAdd")
    public String addInstrumentShow(HttpServletRequest request){
        log.info("进入新增污染源页面成功！");
        return "addInstrument";
    }

}
