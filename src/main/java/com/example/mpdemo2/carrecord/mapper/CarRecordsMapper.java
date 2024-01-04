package com.example.mpdemo2.carrecord.mapper;

import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author Administrator
* @description 针对表【car_records】的数据库操作Mapper
* @createDate 2024-01-03 09:35:11
* @Entity com.example.mpdemo2.carrecord.domain.CarRecords
*/
@Mapper
public interface CarRecordsMapper extends BaseMapper<CarRecords> {

}




