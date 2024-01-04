package com.example.mpdemo2.alarm.alarmrecord.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.alarm.alarmrecord.domain.AlarmRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
* @author Administrator
* @description 针对表【alarm_records(报警记录表)】的数据库操作Service
* @createDate 2024-01-03 10:43:48
*/
@Service
public interface AlarmRecordsService extends IService<AlarmRecords> {
    public PageInfo<AlarmRecords> getAlarmSettingsByPage(Integer pageNum, Integer pageSize, HashMap<String,String> features);
}
