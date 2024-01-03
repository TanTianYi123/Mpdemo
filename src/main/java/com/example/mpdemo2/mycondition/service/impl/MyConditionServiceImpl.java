package com.example.mpdemo2.mycondition.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.mycondition.domain.MyCondition;
import com.example.mpdemo2.mycondition.service.MyConditionService;
import com.example.mpdemo2.mycondition.mapper.MyConditionMapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
* @author Dell
* @description 针对表【my_condition】的数据库操作Service实现
* @createDate 2023-12-28 10:31:01
*/
@Service
@Slf4j
public class MyConditionServiceImpl extends ServiceImpl<MyConditionMapper, MyCondition> implements MyConditionService{
    @Autowired
    MyConditionMapper myConditionMapper;

    public PageInfo<MyCondition>  returnselecttabledata(String instrumentId, String date){
        LocalDate dateDaysAgo;
        List<MyCondition> myConditions;
        QueryWrapper<MyCondition> queryWrapper = new QueryWrapper<>();

        //0用户选择都全部，1用户选择设备，2用户选择时间，3用户选择日期与设备
        int flag = 0;
        if(!instrumentId.equals("all")&&date.equals("all")) flag = 1;
        if(instrumentId.equals("all")&&!date.equals("all")) flag = 2;
        if(!instrumentId.equals("all")&&!date.equals("all")) flag = 3;
        log.info("instrumentId"+ instrumentId);
        log.info("date" + date );
        log.info(String.valueOf(flag));
       switch (flag){
           case 0:
               myConditions = myConditionMapper.searchAllByDeleteFlag(0);
               break;
           case 1:
               myConditions = myConditionMapper.searchAllByInstrumentIdAndDeleteFlag(instrumentId,0);
               break;
           case 2:
               //生成时间判决条件
               dateDaysAgo = LocalDate.now().minusDays(Long.parseLong(date));
               log.info("dateDaysAgo  "+ dateDaysAgo);
               queryWrapper.ge("condition_date", dateDaysAgo).eq("delete_flag", 0);
               myConditions = myConditionMapper.selectList(queryWrapper);
               break;
           case 3:
               dateDaysAgo = LocalDate.now().minusDays(Long.parseLong(date));
               queryWrapper.ge("condition_date", dateDaysAgo).eq("delete_flag", 0).eq("instrument_id",instrumentId);
               myConditions = myConditionMapper.selectList(queryWrapper);
               break;
           default:
               myConditions = myConditionMapper.selectList(null);
               break;
       }



        return new PageInfo<>(myConditions);
    }
}




