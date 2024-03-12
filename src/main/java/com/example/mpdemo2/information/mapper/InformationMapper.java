package com.example.mpdemo2.information.mapper;
import java.util.List;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mpdemo2.information.domain.Information;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Dell
* @description 针对表【information】的数据库操作Mapper
* @createDate 2023-12-28 10:04:45
* @Entity com.example.mpdemo2.information.domain.Information
*/
@Mapper
public interface InformationMapper extends BaseMapper<Information> {
    List<Information> selectAllByIdEquals(@Param("id") Integer id);

    @Override
    int updateById(@Param(Constants.ENTITY) Information entity);
}




