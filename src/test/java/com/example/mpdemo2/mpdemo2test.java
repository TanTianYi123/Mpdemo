package com.example.mpdemo2;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/2 0002 15:15
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.user.service.UserService;
import com.example.mpdemo2.video.domain.Video;
import com.example.mpdemo2.video.service.VideoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/2 0002 15:15
 */
@SpringBootTest
public class mpdemo2test {
    @Autowired
    VideoService videoService;
    @Test
    public  void test(){
        Page<Video> page = new Page<>(1,5);
        videoService.page(page, null);
        System.out.println("---------------------------------------------------");
        System.out.println(page.getRecords());
        System.out.println("---------------------------------------------------");
    }
}
