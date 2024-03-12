package com.example.mpdemo2.alarm.service.impl;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 10:49
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.mpdemo2.alarm.alarmrecord.mapper.AlarmRecordsMapper;
import com.example.mpdemo2.alarm.alarmsetting.domain.AlarmSettings;
import com.example.mpdemo2.alarm.mapper.AlarmMapper;
import com.example.mpdemo2.alarm.service.AlarmService;
import com.example.mpdemo2.util.Constants;
import com.example.mpdemo2.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public PageInfo<AlarmSettings> getAlarmSettingsByPage(Integer pageNum, Integer pageSize, String feature) {
        List<AlarmSettings> alarmSettings;
        if(feature!=null&&!feature.equals("")){
            feature = "%"+feature+"%";
            alarmSettings = alarmMapper.getAlarmSettingsByFeatures(feature);
        }else{
            alarmSettings = alarmMapper.getAllAlarmSettings();
        }
        PageInfo<AlarmSettings> pageInfo = new PageInfo<>(alarmSettings, 5);
        return pageInfo;
    }

    @Override
    public String addAlarmSetting(AlarmSettings alarmSettingsEntity, String newMonitorClass, String existMonitorClass, String newPollution, String existPollutionName) {
        //新增预警设置，判断上下限阈值的合理性和监测值是否重复
        if (!ObjectUtil.isEmpty(alarmSettingsEntity)){
            if (alarmSettingsEntity.getUpperLimit()<= alarmSettingsEntity.getLowerLimit()) return "上限阈值须大于下限阈值！";
            try {
                alarmMapper.insertAlarmEntity(alarmSettingsEntity);
                log.info("新增预警设置提交成功！");
            }catch (DataIntegrityViolationException e){
                log.info("新增预警设置提交失败！");
                log.error("新增预警设置提交失败！",e);
                return "监测值不能和已有的重复！";
            }

            return "新增预警设置成功！";
        }

        //新增监测值，判断是否和已有的监测值重复
        if (newMonitorClass!=null){
            //插入新的监测类型
            if (!ObjectUtil.isEmptyString(existMonitorClass)){
                List<String> allMonitorClass = new ArrayList<>();
                Collections.addAll(allMonitorClass, existMonitorClass.split(","));
                if (allMonitorClass.contains(newMonitorClass)){
                    return "和已有监测类型重复！";
                }
            }
            try {
                log.info("新增监测类型提交成功！");
                alarmMapper.insertMonitorClass(newMonitorClass);
                return "新增监测类型成功！";
            }catch (Exception e){
                log.info("新增监测类型提交失败！");
                log.error("新增监测类型提交失败！",e);
                return "新增监测类型提交失败！";
            }
        }

        //新增污染物，判断是否和已有的污染物重复
        if (newPollution!=null){
            //插入新的污染物
            if(!ObjectUtil.isEmptyString(existPollutionName)){
                List<String> pollutionNameList = Arrays.stream(existPollutionName.split(",")).collect(Collectors.toList());
                if (pollutionNameList.contains(newPollution)){
                    return "和已有污染物重复！";
                }
            }
            try {
                alarmMapper.insertPollution(newPollution);
                log.info("新增污染物提交成功！");
                return "新增污染物提交成功！";
            }catch (Exception e){
                log.info("新增污染物提交成功！");
                log.error("新增污染物提交失败！",e);
                return "新增污染物失败！";
            }
        }
        return "未接收到数据！";
    }
    /**
     * 根据id删除预警设置
     * @param id id
     * @return 返回页面的消息
     */
    public String deleteAlarmSettingById(int id){
        try {
            alarmMapper.deleteAlarmSettingById(id);
            log.info("删除预警设置成功!");
            return "删除预警设置成功！";
        }catch (Exception e){
            log.info("删除预警设置失败!" );
            log.error("删除预警设置失败!",e);
            return "删除预警设置失败";
        }
    }

    @Override
    public String editAlarmSetting(AlarmSettings alarmSettingsEntity) {
        if (alarmSettingsEntity.getUpperLimit()<= alarmSettingsEntity.getLowerLimit()) return "上限阈值须大于下限阈值！";
        if (alarmMapper.getMonitorValue(alarmSettingsEntity.getMonitorValue())!=null) return "监测值和已有监测值重复！";
        try {
            alarmMapper.updateAlarmSetting(alarmSettingsEntity);
            log.info("预警设置成功!" );
            return "修改成功！";

        }catch (Exception e){
            log.info("预警设置失败！");
            log.error("预警设置失败！",e);
            return "预警设置失败！";
        }
    }

    @Override
    public String selectRecordsByDynamicSql(HashMap<String, String> features) {
        String sql = new SQL(){
            {
                SELECT("*");
                FROM("alarm_records");
                if (!ObjectUtil.isEmptyString(features.get("monitor"))){
                    WHERE("monitor = '"+features.get("monitor")+"'");
                }
                if (!ObjectUtil.isEmptyString(features.get("monitorClass"))){
                    WHERE("monitor_class = '"+features.get("monitorClass")+"'");
                }
                if (!ObjectUtil.isEmptyString(features.get("monitorValue"))){
                    WHERE("monitor_value = '"+features.get("monitorValue")+"'");
                }
                if (!ObjectUtil.isEmptyString(features.get("startTime"))){
                    WHERE("alarm_time >= '"+features.get("startTime")+"'");
                }
                if (!ObjectUtil.isEmptyString(features.get("endTime"))){
                    WHERE("alarm_time <= '"+features.get("endTime")+"'");
                }
            }
        }.toString();
        return sql;
    }

}
