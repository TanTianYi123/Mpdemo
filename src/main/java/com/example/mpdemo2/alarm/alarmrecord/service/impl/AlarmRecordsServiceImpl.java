package com.example.mpdemo2.alarm.alarmrecord.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.alarm.alarmrecord.mapper.AlarmRecordsMapper;
import com.example.mpdemo2.alarm.alarmrecord.service.AlarmRecordsService;
import com.example.mpdemo2.alarm.alarmrecord.domain.AlarmRecords;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
* @author Administrator
* @description 针对表【alarm_records(报警记录表)】的数据库操作Service实现
* @createDate 2024-01-03 10:43:48
*/
@Service
public class AlarmRecordsServiceImpl extends ServiceImpl<AlarmRecordsMapper, AlarmRecords>
    implements AlarmRecordsService {
    @Autowired
    AlarmRecordsMapper alarmRecordsMapper;

    public PageInfo<AlarmRecords> getAlarmSettingsByPage(Integer pageNum, Integer pageSize, HashMap<String,String> features) {
        List<AlarmRecords> alarmRecords = alarmRecordsMapper.selectList(null);
        PageInfo<AlarmRecords> pageinfo = new PageInfo<>(alarmRecords);

        return pageinfo;
    }
}




