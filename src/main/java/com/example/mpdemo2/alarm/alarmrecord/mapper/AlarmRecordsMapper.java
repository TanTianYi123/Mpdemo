package com.example.mpdemo2.alarm.alarmrecord.mapper;

import com.example.mpdemo2.alarm.alarmrecord.domain.AlarmRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【alarm_records(报警记录表)】的数据库操作Mapper
* @createDate 2024-01-03 10:43:48
* @Entity com.example.mpdemo2.alarmrecord.domain.AlarmRecords
*/
@Mapper
public interface AlarmRecordsMapper extends BaseMapper<AlarmRecords> {

}




