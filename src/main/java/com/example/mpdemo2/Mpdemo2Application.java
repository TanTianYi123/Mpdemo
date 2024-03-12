package com.example.mpdemo2;

import com.example.mpdemo2.util.MqttUtils.BootNettyMqttServerThread;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.example.mpdemo2.*.mapper","com.example.mpdemo2.*.*.mapper"})
public class Mpdemo2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Mpdemo2Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        int port = 1883;
        BootNettyMqttServerThread bootNettyMqttServerThread = new BootNettyMqttServerThread(port);
        bootNettyMqttServerThread.start();
    }
}
