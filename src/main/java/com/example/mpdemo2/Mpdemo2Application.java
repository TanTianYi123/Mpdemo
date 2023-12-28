package com.example.mpdemo2;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.example.mpdemo2.*.mapper")
public class Mpdemo2Application {

    public static void main(String[] args) {
        SpringApplication.run(Mpdemo2Application.class, args);
    }

}
