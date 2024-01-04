package com.example.mpdemo2.alarm.alarmsetting.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 预警设置
 * @TableName alarm_settings
 */
@TableName(value ="alarm_settings")
@Data
public class AlarmSettings implements Serializable {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 监测类型
     */
    private String monitorClass;

    /**
     * 监测值
     */
    private String monitorValue;

    /**
     * 下限阈值
     */
    private Double lowerLimit;

    /**
     * 上限阈值
     */
    private Double upperLimit;

    /**
     * 单位
     */
    private String unit;

    /**
     * 告警信息
     */
    private String message;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}