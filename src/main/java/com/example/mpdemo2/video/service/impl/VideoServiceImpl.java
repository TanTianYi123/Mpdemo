package com.example.mpdemo2.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.video.domain.Video;
import com.example.mpdemo2.video.service.VideoService;
import com.example.mpdemo2.video.mapper.VideoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【video】的数据库操作Service实现
* @createDate 2024-01-02 14:59:12
*/
@Service
@Slf4j
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{

}




