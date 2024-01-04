package com.example.mpdemo2.facerecord.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.example.mpdemo2.facerecord.service.FaceRecordsService;
import com.example.mpdemo2.facerecord.mapper.FaceRecordsMapper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author Administrator
* @description 针对表【face_records】的数据库操作Service实现
* @createDate 2024-01-03 09:32:40
*/
@Service
public class FaceRecordsServiceImpl extends ServiceImpl<FaceRecordsMapper, FaceRecords>
    implements FaceRecordsService{
    @Autowired
    FaceRecordsMapper faceRecordsMapper;
    public PageInfo<FaceRecords> selectFaceRecordsByPage(Integer pageSize, Integer pageNum){
        List<FaceRecords> faceRecords = faceRecordsMapper.selectList(null);
        return new PageInfo<>(faceRecords);
    }
}




