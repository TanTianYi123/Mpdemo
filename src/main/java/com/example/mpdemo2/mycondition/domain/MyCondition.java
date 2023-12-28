package com.example.mpdemo2.mycondition.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName my_condition
 */
@TableName(value ="my_condition")
@Data
public class MyCondition implements Serializable {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private Date conditionDate;

    /**
     * 重置时间
     */
    private Date resttime;

    /**
     * 
     */
    private String conditionWorking;

    /**
     * 
     */
    private Double conditionValue;

    /**
     * 
     */
    private String instrumentId;

    /**
     * 
     */
    private Integer deleteFlag;

    /**
     * 
     */
    private Long companyId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}