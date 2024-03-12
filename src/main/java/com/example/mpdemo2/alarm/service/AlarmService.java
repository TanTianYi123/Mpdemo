package com.example.mpdemo2.alarm.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mpdemo2.alarm.alarmrecord.mapper.AlarmRecordsMapper;
import com.example.mpdemo2.alarm.alarmrecord.service.AlarmRecordsService;
import com.example.mpdemo2.alarm.alarmsetting.domain.AlarmSettings;
import com.example.mpdemo2.alarm.alarmsetting.service.AlarmSettingsService;
import com.example.mpdemo2.video.domain.Video;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
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
    public PageInfo<AlarmSettings> getAlarmSettingsByPage(Integer pageNum, Integer pageSize, String feature);
    public String addAlarmSetting(AlarmSettings alarmSettingsEntity,
                                  String newMonitorClass, String existMonitorClass,
                                  String newPollution, String existPollutionName);
    public String deleteAlarmSettingById(int id);
    public String editAlarmSetting(AlarmSettings alarmSettingsEntity);
    public String selectRecordsByDynamicSql(HashMap<String,String> features);
}
