package com.example.mpdemo2.facerecord.service;

import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

/**
* @author Administrator
* @description 针对表【face_records】的数据库操作Service
* @createDate 2024-01-03 09:32:40
*/
public interface FaceRecordsService extends IService<FaceRecords> {
    public PageInfo<FaceRecords> selectFaceRecordsByPage(Integer pageSize, Integer pageNum);
}
