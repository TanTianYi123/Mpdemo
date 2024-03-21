package com.example.mpdemo2.video.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName video
 */
@TableName(value ="video")
@Data
public class Video implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String monitorName;

    /**
     * 
     */
    private String rtsp;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private String stream;

    /**
     * 
     */
    private Integer companyId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public Video(Object o, String monitorName, String rtsp, String description, String stream, boolean personAi, boolean carAi) {
    }
}