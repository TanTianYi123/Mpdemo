package com.example.mpdemo2.alarm.controller;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 10:47
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.alarm.alarmrecord.domain.AlarmRecords;
import com.example.mpdemo2.alarm.alarmrecord.service.AlarmRecordsService;
import com.example.mpdemo2.alarm.alarmsetting.domain.AlarmSettings;
import com.example.mpdemo2.alarm.service.AlarmService;
import com.example.mpdemo2.util.Constants;
import com.example.mpdemo2.util.DateUtil;
import com.example.mpdemo2.util.ObjectUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.HashMap;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 10:47
 */
@Controller
@Slf4j
@RequestMapping("/alarm")
public class AlarmController {
    @Autowired
    AlarmRecordsService alarmRecordsService;
    @Autowired
    AlarmService alarmService;
    @RequestMapping(value = {"/records","/records/{pageNum}/{pageSize}"})
    public String toAlarmRecordsPage(HttpServletRequest request,
                                     @PathVariable(required = false) Integer pageNum,
                                     @PathVariable(required = false) Integer pageSize
    ){
        //条件查询的HashMap，若没有条件查询默认为null
        HashMap<String,String> features = null;
        //条件查询是POST方法，此处判断POST
        if (request.getMethod().equals("POST")){
            //获取查询条件，并放置到HashMap中
            features = new HashMap<>();
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            try {
                //开始结束时间转化为时间戳
                if (!ObjectUtil.isEmptyString(startTime)) features.put("startTime", DateUtil.toTimestamp(startTime));
                if (!ObjectUtil.isEmptyString(endTime)) features.put("endTime",DateUtil.toTimestamp(endTime));
            }catch (ParseException e){
                log.error("转化为时间戳失败！",e);
            }
            //监测点、监测类型、监测值
            String monitor = request.getParameter("monitor");
            if(!ObjectUtil.isEmptyString(monitor)) features.put("monitor",monitor);
            String monitorClass = request.getParameter("monitorClass");
            if(!ObjectUtil.isEmptyString(monitorClass)) features.put("monitorClass",monitorClass);
            String monitorValue = request.getParameter("monitorValue");
            if(!ObjectUtil.isEmptyString(monitorValue)) features.put("monitorValue",monitorValue);

            //返回前端，方便下一次筛选
            request.setAttribute("monitor", features.get("monitor"));
            request.setAttribute("monitorClass", features.get("monitorClass"));
            request.setAttribute("monitorValue", features.get("monitorValue"));
            request.setAttribute("startTime", startTime);
            request.setAttribute("endTime", endTime);
        }
        //查询数据库
        PageInfo<AlarmRecords> allAlarmRecords = alarmRecordsService.getAlarmSettingsByPage(pageNum, pageSize, features);
        if(allAlarmRecords == null) {
            log.info("报警记录页面成功！" );
            return "500";
        }
        if (!alarmService.setAttributeBYMonitorAndPollution(request)){
            log.info("报警记录页面失败！" );
            log.error("报警记录页面失败！");
            return "500";
        }
        request.setAttribute("records", allAlarmRecords);
        return Constants.ALARMREOCRDS;
    }
    /**
     * 跳转到预警设置页面
     * 查询现有所有预警设置，返回前端
     * @param request http请求
     * @param pageNum 分页插件：当前页码数
     * @param pageSize 分页插件：每页大小
     * @param feature 模糊查询，查询关键字
     * @return 预警设置页面
     */
    @RequestMapping(value = {"/settings","/settings/{pageNum}/{pageSize}"})
    public String toAlarmSettingsPage(HttpServletRequest request,
                                      @PathVariable(required = false) Integer pageNum,
                                      @PathVariable(required = false) Integer pageSize,
                                      String feature){
        PageInfo<AlarmSettings> alarmSettings = alarmService.getAlarmSettingsByPage(pageNum, pageSize, feature);

        if(alarmSettings == null){
            log.info("进入预警设置页面失败！");
            log.error("进入预警设置页面失败！");
            return "500";
        }else {
            log.info("进入预警设置页面成功！");
            request.setAttribute("allSettings",alarmSettings);
            return Constants.ALARMSETTINGS;
        }

    }
    /**
     * 跳转到新增预警设置页面
     * @param request request
     * @return 新增预警页面
     */
    @GetMapping("/settings/add")
    public String toAddAlarmSettingPage(HttpServletRequest request){
        if (!alarmService.setAttributeBYMonitorAndPollution(request)){
            log.info("进入新增预警设置页面失败！");
            log.error("进入新增预警设置页面失败！");
            return "500";
        }else {
            log.info("进入新增预警设置页面成功！");
        }
        return Constants.ADDALARMSETTING;
    }
    /**
     * 新增预警设置或监测类型或污染物类型
     * 预警设置中，监测值不能重复
     * 监测类型和污染物类型不能和已有的重复
     * @param alarmSettingsEntity 新预警设置
     * @param newMonitorClass 新监测类型
     * @param existMonitorClass 已有的监测类型
     * @param newPollution 新污染物
     * @param existPollutionName 已有的污染物
     * @return 操作成功返回预警设置页，否则返回新增页
     */
    @PostMapping("/settings/add")
    public String addAlarmSetting(HttpServletRequest request, AlarmSettings alarmSettingsEntity,
                                  String newMonitorClass, String newPollution,
                                  String existPollutionName, String existMonitorClass){
        String msg = alarmService.addAlarmSetting(alarmSettingsEntity,newMonitorClass,existMonitorClass,newPollution,existPollutionName);
        request.setAttribute(Constants.MSG, msg);
        if (msg.contains("成功")) return "forward:/alarm/settings";
        else return toAddAlarmSettingPage(request);
    }
    /**
     * 删除预警设置
     * @param request request
     * @param id id
     * @return 预警设置页
     */
    @GetMapping("/settings/delete/{id}")
    public String deleteAlarmSetting(HttpServletRequest request, @PathVariable int id){
        String msg = alarmService.deleteAlarmSettingById(id);
        request.setAttribute(Constants.MSG, msg);
        return "forward:/alarm/settings";
    }
    /**
     * 跳转到预警设置编辑页面
     * @param request request
     * @param alarmSettingsEntity 修改前的预警设置
     * @return 预警设置编辑页
     */
    @GetMapping("/settings/edit")
    public String toEditAlarmSettingPage(HttpServletRequest request , AlarmSettings alarmSettingsEntity){
        System.out.println("*************************************************************************");
        request.setAttribute("alarmSettingsEntity", alarmSettingsEntity);

        if (!alarmService.setAttributeBYMonitorAndPollution(request)){
            return "500";
        }
        return Constants.EDITALARMSETTING;
    }

    /**
     * 编辑预警设置
     * @param request request
     * @param alarmSettingsEntity 修改后的预警设置
     * @return 成功返回预警设置页面，否则返回编辑页面
     */
    @PostMapping("/settings/edit")
    public String editAlarmSetting(HttpServletRequest request, AlarmSettings alarmSettingsEntity){
        String msg = alarmService.editAlarmSetting(alarmSettingsEntity);
        request.setAttribute(Constants.MSG, msg);
        if (msg.contains("成功")) return "forward:/alarm/settings";
        else return toEditAlarmSettingPage(request, alarmSettingsEntity);

    }
}
