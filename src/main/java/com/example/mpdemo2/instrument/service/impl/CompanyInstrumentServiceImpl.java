package com.example.mpdemo2.instrument.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.instrument.domain.CompanyInstrument;
import com.example.mpdemo2.instrument.service.CompanyInstrumentService;
import com.example.mpdemo2.instrument.mapper.CompanyInstrumentMapper;
import org.springframework.stereotype.Service;

/**
* @author Dell
* @description 针对表【company_instrument】的数据库操作Service实现
* @createDate 2024-01-05 11:33:17
*/
@Service
public class CompanyInstrumentServiceImpl extends ServiceImpl<CompanyInstrumentMapper, CompanyInstrument>
    implements CompanyInstrumentService{

}




