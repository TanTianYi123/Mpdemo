package com.example.mpdemo2.alarm.service.impl;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 10:49
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mpdemo2.alarm.alarmrecord.mapper.AlarmRecordsMapper;
import com.example.mpdemo2.alarm.mapper.AlarmMapper;
import com.example.mpdemo2.alarm.service.AlarmService;
import com.example.mpdemo2.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 10:49
 */

@Slf4j
@Service
public class AlarmServiceImpl implements AlarmService {
    @Autowired
    AlarmMapper alarmMapper;
    public boolean setAttributeBYMonitorAndPollution(HttpServletRequest request){
        List<String> allMonitorClass = getAllMonitorClass();
        List<String> allPollutionName = getAllPollutionName();
        List<String> allMonitor = getAllMonitor();
        if (allPollutionName==null || allMonitorClass==null){
            log.error("获取全部监测类型和污染物名称失败！");
            return false;
        }
        log.error("获取全部监测类型和污染物名称成功！");
        request.setAttribute(Constants.ALLMONITORCLASS, allMonitorClass);
        request.setAttribute(Constants.ALLPOLLUTIONNAME, allPollutionName);
        request.setAttribute("allMonitor", allMonitor);
        return true;
    }

    public List<String> getAllMonitorClass(){
        try {
            List<String> allMonitorClass = alarmMapper.getAllMonitorClass();
            return allMonitorClass;
        }catch (Exception e){
            log.error("获取全部监测类型失败！",e);
            return null;
        }
    }
    public List<String> getAllPollutionName(){
        try {
            List<String> allPollutionName = alarmMapper.getAllPollutionName();
            return allPollutionName;
        }catch (Exception e){
            log.error("获取全部污染物名称失败！",e);
            return null;
        }
    }
    public List<String> getAllMonitor(){
        try {
            List<String> allMonitor = alarmMapper.getAllMonitor();
            return allMonitor;
        }catch (Exception e){
            log.error("获取监测点名称失败！",e);
            return null;
        }
    }

}
