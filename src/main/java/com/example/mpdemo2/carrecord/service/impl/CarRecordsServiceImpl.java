package com.example.mpdemo2.carrecord.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.example.mpdemo2.carrecord.service.CarRecordsService;
import com.example.mpdemo2.carrecord.mapper.CarRecordsMapper;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【car_records】的数据库操作Service实现
* @createDate 2024-01-03 09:35:11
*/
@Service
public class CarRecordsServiceImpl extends ServiceImpl<CarRecordsMapper, CarRecords>
    implements CarRecordsService{
    @Autowired
    CarRecordsMapper carRecordsMapper;
    @Override
    public PageInfo<CarRecords> selectCarRecordsByPage(Integer pageSize, Integer pageNum) {
        List<CarRecords> carRecords = carRecordsMapper.selectList(null);
        PageInfo<CarRecords> pageInfo = new PageInfo<>(carRecords);
        return pageInfo;
    }
}




