package com.example.mpdemo2.video.mapper;
import java.util.List;

import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import org.apache.ibatis.annotations.*;

import com.example.mpdemo2.video.domain.Video;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【video】的数据库操作Mapper
* @createDate 2024-01-02 14:59:12
* @
 com.example.mpdemo2.video.domain.Video
*/
@Mapper
public interface VideoMapper extends BaseMapper<Video> {
    List<Video> selectAllByMonitorName(@Param("monitorName") String monitorName);

    List<Video> selectAllByStream(@Param("stream") String stream);
    /**
     * 插入一个摄像头
     * @param video
     */
    @Insert("INSERT INTO `video` VALUES (null, #{monitorName}, #{rtsp}, #{description},#{stream},#{personAi},#{carAi})")
    int insertIntoVideo(Video
 video);
    @Delete("delete from `video` where rtsp = #{rtsp}")
    void deleteVideo(String rtsp);
    /**
     * 查询全部摄像头
     * @return
     */
    @Select("SELECT * FROM `video`")
    List<Video
> selectAllVideos();

    /**
     * 根据特征feature模糊查询摄像头信息
     * @param feature
     * @return
     */
    @Select("SELECT * FROM `video` where monitor_name like #{feature} or description like #{feature}")
    List<Video
> selectVideosByFeature(String feature);

    /**
     * 根据stream查询rtsp
     * @param stream
     * @return
     */
    @Select("SELECT `rtsp` FROM `video` WHERE `stream`=#{stream}")
    String selectRtspByStream(String stream);

    @Insert("INSERT INTO `face_records` (`picture_url`, `result`, `score`, `time`) VALUES(#{pictureUrl},#{result},#{score},#{time})")
    void insertFaceRecord(FaceRecords
 faceRecords);

    @Select("SELECT * FROM face_records")
    List<FaceRecords
> selectAllFaceRecords();


    @Insert("INSERT INTO car_records (picture_url,result,score) VALUES(#{pictureUrl},#{result},#{score})")
    void insertCarRecord(CarRecords
 carRecords);
    @Select("SELECT * FROM car_records")
    List<CarRecords
> selectAllCarRecords();


    @Select("SELECT * FROM face_records WHERE time>DATE_SUB(NOW(), INTERVAL 1 DAY ) AND time<=NOW();")
    List<FaceRecords
> selectTodayFaceRecordsTime();

    @Select("SELECT * FROM car_records WHERE time>DATE_SUB(NOW(), INTERVAL 1 DAY ) AND time<=NOW();")
    List<CarRecords
> selectTodayCarRecordsTime();


}




