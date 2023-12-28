package com.example.mpdemo2.information.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName information
 */
@TableName(value ="information")
@Data
public class Information implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String company;

    /**
     * 
     */
    private String industry;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String location;

    /**
     * 
     */
    private String telephone;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}