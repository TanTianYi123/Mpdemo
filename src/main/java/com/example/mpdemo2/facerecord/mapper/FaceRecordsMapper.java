package com.example.mpdemo2.facerecord.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【face_records】的数据库操作Mapper
* @createDate 2024-01-03 09:32:40
* @Entity com.example.mpdemo2.facerecord.domain.FaceRecords
*/
@Mapper
public interface FaceRecordsMapper extends BaseMapper<FaceRecords> {

}




