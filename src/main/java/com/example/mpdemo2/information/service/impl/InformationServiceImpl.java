package com.example.mpdemo2.information.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.information.domain.Information;
import com.example.mpdemo2.information.service.InformationService;
import com.example.mpdemo2.information.mapper.InformationMapper;
import org.springframework.stereotype.Service;

/**
* @author Administrator
* @description 针对表【information】的数据库操作Service实现
* @createDate 2023-12-28 16:28:14
*/
@Service
public class InformationServiceImpl extends ServiceImpl<InformationMapper, Information>
    implements InformationService{

}




