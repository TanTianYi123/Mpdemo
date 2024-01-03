package com.example.mpdemo2.mycondition.mapper;
import java.util.Date;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.example.mpdemo2.mycondition.domain.MyCondition;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author Dell
* @description 针对表【my_condition】的数据库操作Mapper
* @createDate 2023-12-28 10:31:01
* @Entity com.example.mpdemo2.mycondition.domain.MyCondition
*/
@Mapper
public interface MyConditionMapper extends BaseMapper<MyCondition> {
    @Override
    List<MyCondition> selectList(@Param(Constants.WRAPPER) Wrapper<MyCondition> queryWrapper);

    List<MyCondition> searchAllByInstrumentIdAndDeleteFlag(@Param("instrumentId") String instrumentId, @Param("deleteFlag") Integer deleteFlag);

    List<MyCondition> searchAllByDeleteFlag(@Param("deleteFlag") Integer deleteFlag);


}




