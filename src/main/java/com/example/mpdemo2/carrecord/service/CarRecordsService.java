package com.example.mpdemo2.carrecord.service;

import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.github.pagehelper.PageInfo;

/**
* @author Administrator
* @description 针对表【car_records】的数据库操作Service
* @createDate 2024-01-03 09:35:11
*/
public interface CarRecordsService extends IService<CarRecords> {
    public PageInfo<CarRecords> selectCarRecordsByPage(Integer pageSize, Integer pageNum);
}
