package com.example.mpdemo2.alarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mpdemo2.alarm.alarmrecord.mapper.AlarmRecordsMapper;
import com.example.mpdemo2.alarm.alarmrecord.service.AlarmRecordsService;
import com.example.mpdemo2.alarm.alarmsetting.service.AlarmSettingsService;
import com.example.mpdemo2.video.domain.Video;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 10:51
 */
@Service
public interface AlarmService{
    public boolean setAttributeBYMonitorAndPollution(HttpServletRequest request);
    public List<String> getAllMonitorClass();
    public List<String> getAllPollutionName();
    public List<String> getAllMonitor();

}
