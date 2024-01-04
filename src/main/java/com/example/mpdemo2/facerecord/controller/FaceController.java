package com.example.mpdemo2.facerecord.controller;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 9:42
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.example.mpdemo2.facerecord.service.FaceRecordsService;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 9:42
 */
@Slf4j
@Controller
public class FaceController {
    @Autowired
    FaceRecordsService recordsService;
    @RequestMapping({"/videoMonitor/face","/videoMonitor/face/{pageSize}/{pageNum}"})
    public String toFaceRecordsOfVideoPage(HttpServletRequest request,
                                           @PathVariable(required = false) Integer pageSize,
                                           @PathVariable(required = false) Integer pageNum ){
        PageInfo<FaceRecords> faceRecordsPageInfo = recordsService.selectFaceRecordsByPage(pageSize, pageNum);
        request.setAttribute("class","face");
        request.setAttribute("faceRecords",faceRecordsPageInfo);

        log.info("进入人员识别结果页面成功！");
        return "video";
    }

}
