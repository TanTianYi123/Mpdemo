package com.example.mpdemo2.video.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mpdemo2.video.domain.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【video】的数据库操作Mapper
* @createDate 2024-01-02 14:59:12
* @Entity com.example.mpdemo2.video.domain.Video
*/
@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    List<Video> selectAllByMonitorName(@Param("monitorName") String monitorName);

    List<Video> selectAllByStream(@Param("stream") String stream);

}




