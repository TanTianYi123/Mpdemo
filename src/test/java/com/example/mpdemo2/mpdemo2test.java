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

import java.util.Queue;
import java.util.Scanner;

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
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // 注意 hasNext 和 hasNextLine 的区别
        int len = in.nextInt();
        int q = in.nextInt();
        int[] array = new int[len];
        int i = 0;
        while (q>0&& in.hasNextInt()) { // 注意 while 处理多个 case
            if(i<len){
                array[i++] = in.nextInt();
            }else{
                int l = in.nextInt();
                int r = in.nextInt();
                int[] rst = find(array,l,r);
                System.out.print(rst[0]+" "+rst[1]);
                q--;
            }
        }
    }
    public static int[] find(int[] input,int l,int r){
        int rst =0;
        int min = 0;
        int max = 0;
        for(int i=0;i<input.length;i++){
            if(input[i]!=0) rst+=input[i];
            else{
                min += l;
                max += r;
            }
        }
        return new int[]{rst+min,rst+max};
    }

}
