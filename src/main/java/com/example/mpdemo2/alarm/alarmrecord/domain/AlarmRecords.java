package com.example.mpdemo2.alarm.alarmrecord.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 报警记录表
 * @TableName alarm_records
 */
@TableName(value ="alarm_records")
@Data
public class AlarmRecords implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 报警时间
     */
    private Date alarmTime;

    /**
     * 监测点
     */
    private String monitor;

    /**
     * 监测类型
     */
    private String monitorClass;

    /**
     * 监测值
     */
    private String monitorValue;

    /**
     * 监测数据
     */
    private String monitorData;

    /**
     * 
     */
    private String unit;

    /**
     * 告警信息
     */
    private String message;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}