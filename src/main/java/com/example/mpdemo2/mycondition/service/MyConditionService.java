package com.example.mpdemo2.mycondition.service;

import com.example.mpdemo2.mycondition.domain.MyCondition;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;

import java.text.SimpleDateFormat;
import java.util.List;

/**
* @author Dell
* @description 针对表【my_condition】的数据库操作Service
* @createDate 2023-12-28 10:31:01
*/
public interface MyConditionService extends IService<MyCondition> {
    public List<MyCondition> returnselecttabledata(String instrumentId, String date);

}
