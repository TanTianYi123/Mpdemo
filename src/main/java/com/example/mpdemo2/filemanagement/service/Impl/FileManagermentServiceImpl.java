package com.example.mpdemo2.filemanagement.service.Impl;

import com.example.mpdemo2.video.domain.Video;
import com.example.mpdemo2.video.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileManagermentServiceImpl {
    @Autowired
    VideoMapper videoMapper;

}
