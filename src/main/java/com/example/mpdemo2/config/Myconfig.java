package com.example.mpdemo2.config;/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/22 0022 15:56
 */

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description:
 * @author: Administrator
 * @time: 2023/12/22 0022 15:56
 */
@Configuration
@MapperScan("com.example.mpdemo2.mapper")
public class Myconfig {
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }
}
