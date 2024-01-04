package com.example.mpdemo2.alarm.mapper;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 14:27
 */

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 14:27
 */
@Mapper
public interface AlarmMapper {

    @Select("select name from monitor_class")
    public List<String> getAllMonitorClass();

    @Select("select name from monitor")
    public List<String> getAllMonitor();

    @Select("select name from pollution")
    public List<String> getAllPollutionName();
}
