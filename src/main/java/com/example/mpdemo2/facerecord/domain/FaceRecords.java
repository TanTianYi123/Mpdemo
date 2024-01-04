package com.example.mpdemo2.facerecord.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName face_records
 */
@TableName(value ="face_records")
@Data
public class FaceRecords implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String pictureUrl;

    /**
     * 
     */
    private String result;

    /**
     * 
     */
    private Double score;

    /**
     * 
     */
    private Date time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}