package com.example.mpdemo2.video.controller;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/2 0002 15:42
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.example.mpdemo2.video.domain.Video;
import com.example.mpdemo2.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.jfr.Description;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/2 0002 15:42
 */
@Slf4j
@Api(tags = "视频接口")
@Controller
@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;
    /*
     * @description:分页查询
            * @return: java.lang.String
            * @author: Mr.T
            * @time: 2024/1/2 0002 16:30
     */
    @ApiOperation("视频分页查询接口")
    @RequestMapping("videoMonitor")
    @Description("跳转到摄像头列表页面")
    public String toVideoPage(Video video,HttpServletRequest request){


        Page<Video> page = new Page<>(1,5);
        videoService.page(page,null);
        List<Video> videos = page.getRecords();
        request.setAttribute("class","video");
        request.setAttribute("allVideos",videos);
        return "video";
    }


}
