package com.example.mpdemo2.mycondition.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.mycondition.domain.MyCondition;
import com.example.mpdemo2.mycondition.service.MyConditionService;
import com.example.mpdemo2.mycondition.mapper.MyConditionMapper;
import org.springframework.stereotype.Service;

/**
* @author Dell
* @description 针对表【my_condition】的数据库操作Service实现
* @createDate 2023-12-28 10:31:01
*/
@Service
public class MyConditionServiceImpl extends ServiceImpl<MyConditionMapper, MyCondition>
    implements MyConditionService{

}




