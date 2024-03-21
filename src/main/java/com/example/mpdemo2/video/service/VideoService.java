package com.example.mpdemo2.video.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.video.domain.Video;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Administrator
* @description 针对表【video】的数据库操作Service
* @createDate 2024-01-02 14:59:12
*/
public interface VideoService extends IService<Video> {
    public int insertVideo(String monitorName,
                           String username,
                           String password,
                           String ip,
                           String port,
                           String description,
                           boolean personAi,
                           boolean carAi);
    public void deleteVideo(String rtsp);



}
