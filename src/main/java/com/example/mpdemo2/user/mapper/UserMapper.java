package com.example.mpdemo2.user.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.mpdemo2.user.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Mapper
* @createDate 2023-12-27 14:12:04
* @Entity com.example.mpdemo2.user.domain.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> selectAllByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    List<User> selectByUserName(@Param("userName") String userName);

    List<User> selectAllByUserName(@Param("userName") String userName);

}




