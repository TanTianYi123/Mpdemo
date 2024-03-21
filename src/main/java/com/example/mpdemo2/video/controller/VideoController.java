package com.example.mpdemo2.video.controller;/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/2 0002 15:42
 */

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.example.mpdemo2.util.ClientDemo;
import com.example.mpdemo2.util.IpUtil;
import com.example.mpdemo2.util.ObjectUtil;
import com.example.mpdemo2.video.domain.Video;
import com.example.mpdemo2.video.service.VideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.Inet4Address;
import java.net.SocketException;
import java.util.List;

/**
 * @description:
 * @author: Administrator
 * @time: 2024/1/2 0002 15:42
 */
@Slf4j
@Api(tags = "视频接口")
@Controller
@CrossOrigin
public class VideoController {
    @Autowired
    private VideoService videoService;
    /*
     * @description:分页查询
            * @return: java.lang.String
            * @author: Mr.T
            * @time: 2024/1/2 0002 16:30
     */
    @ApiOperation("视频分页查询接口")
    @RequestMapping("videoMonitor")

    public String toVideoPage(Video video,HttpServletRequest request){
        Page<Video> page = new Page<>(1,5);
        videoService.page(page,null);
        List<Video> videos = page.getRecords();
        request.setAttribute("class","video");
        request.setAttribute("allVideos",videos);
        return "video";
    }
    @GetMapping("/addVideo")
    public String toAddVideoPage(){
        log.info("进入添加摄像头页面成功！");
        return "addVideo";
    }
    @PostMapping("/addVideo")
    public String addVideo(@RequestParam String monitorName,
                           @RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String ip,
                           @RequestParam(required = false,defaultValue = "true") boolean personAi,
                           @RequestParam(required = false,defaultValue = "false") boolean carAi,
                           String port,
                           String description,
                           HttpServletRequest request) throws InterruptedException {
        if (ObjectUtil.isEmptyString(port))port="554";
        ClientDemo client = new ClientDemo();
        String message = client.HCIsAvailable(ip,Short.parseShort(port),username,password);

        if (message.contains("成功")){
            try {
                int result = videoService.insertVideo(monitorName,username,password,ip,port,description,personAi,carAi);
                if (result>0){
                    log.info("添加摄像头成功！");
                    Thread.sleep(2000);
                    return toVideoPage((Video) request,null);
                }
            }catch (Exception e){
                log.error("添加摄像头失败！",e);
                request.setAttribute("error","数据库错误！请检查网络或摄像头信息是否和已有摄像头重复！");
            }
        }else{
            log.error("添加摄像头失败！");
            request.setAttribute("error","连接摄像头失败！请检查ip、端口、用户名、密码是否正确！");
        }
        request.setAttribute("ip",ip);
        request.setAttribute("monitorName",monitorName);
        request.setAttribute("username",username);
        request.setAttribute("port",port);
        request.setAttribute("description",description);
        return "addVideo";

    }
    /**
     * 跳转到视频播放页面
     * @param stream rtmp流中的stream参数
     * @return 视频播放页面
     */
    @GetMapping("/videoPlay")
    public String toVideoPlayPage(String stream, HttpServletRequest request) throws SocketException {

        request.setAttribute("stream",new String[]{stream});
        Inet4Address inet4Address = IpUtil.getLocalIp4Address().orElse(null);
        String ip = inet4Address.toString().substring(1);
        if (ip.length()>0){
            request.setAttribute("ip",ip);
        }else {
            request.setAttribute("msg","获取服务器ip地址失败！");
        }
        //		videoService.addSession(stream);
        log.info("进入摄像头实时监测页面成功！");
        return "videoPlay";
//		return stream;
//		return stream;
    }


    @RequestMapping("/videoDelete")
    public String videoDelete(String rtsp, HttpServletRequest request) {
//		System.out.println(rtsp);
        try {
            videoService.deleteVideo(rtsp);
            //删除注册的bean
            //删除ffmpeh进程
            request.setAttribute("msg","摄像头删除成功！");

        }catch (Exception e){
            request.setAttribute("msg","摄像头删除失败！");
        }
        return  toVideoPage((Video) request, null);
    }


    /**
     * 退出视频播放页面，用于推流进程的开启关闭管理
     * @param stream rtmp流中的stream参数
     * @return 啥也不返回
     */
	/*@PostMapping("/quitVideoPlay")
	@ResponseBody
	public String quitVideoPlayPage(@RequestParam String stream){
		videoService.dropSession(stream);
		return null;
	}*/



}
