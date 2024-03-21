package com.example.mpdemo2.video.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 进程类
 * 每个url一个推流进程足够
 * 该类用来记录推流进程的进程id，和正在使用该流的页面数量
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FfmpegProcess {

    /**
     * 进程id
     */
    private int pid;

    /**
     * 正在拉流的s页面数量
     */
    private int count;

    public int increase(){
        return count++;
    }

    public int decrease(){
        return count--;
    }
}
