package com.example.mpdemo2.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private String phone;

    /**
     * 
     */
    private String email;

    /**
     * 
     */
    private Integer gender;

    /**
     * 
     */
    private String icon;

    /**
     * 
     */
    private String number;

    /**
     * 
     */
    private Integer postId;

    /**
     * 
     */
    private String postName;

    /**
     * 
     */
    private Date created;

    /**
     * 
     */
    private Date updated;

    /**
     * 
     */
    private Integer companyId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}