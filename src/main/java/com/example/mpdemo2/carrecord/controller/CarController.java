package com.example.mpdemo2.carrecord.controller;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/3 0003 9:42
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.example.mpdemo2.carrecord.service.CarRecordsService;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
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
public class CarController {
    @Autowired
    CarRecordsService carRecordsService;
    @RequestMapping({"/videoMonitor/car","/videoMonitor/car/{pageSize}/{pageNum}"})
    public String toCarRecordsOfVideoPage(HttpServletRequest request,
                                          @PathVariable(required = false) Integer pageSize,
                                          @PathVariable(required = false) Integer pageNum){

        PageInfo<CarRecords> pageInfo = carRecordsService.selectCarRecordsByPage(pageSize, pageNum);
        request.setAttribute("class","car");
        request.setAttribute("carRecords",pageInfo);
        log.info("进入车牌识别结果页面成功！");
        return "video";
    }
}
