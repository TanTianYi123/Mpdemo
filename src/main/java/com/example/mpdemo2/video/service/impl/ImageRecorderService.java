package com.example.mpdemo2.video.service.impl;


import com.example.mpdemo2.util.Constants;
import com.example.mpdemo2.util.ProcessUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author lbx
 * @date 2023/2/27 - 15:04
 **/

/**
 * 连续截图，覆盖截图
 * @author eguid
 */

@Slf4j
@Service
@NoArgsConstructor
@Getter
public class ImageRecorderService implements Runnable{



    private String input;
    private String output;
    private String monitorName;
    private Integer width;
    private Integer height;
    private String mode;

    private static SimpleDateFormat DataFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static String url = Constants.IMGOUTPUTPATH;

    //该方法监测的文件夹路径
    private String PARENT_DIR;


    /**
     * 视频快照，连续截图，覆盖截图
     * @author eguid
     * @param input 可以是动态图片(apng,gif等等)，视频文件（mp4,flv,avi等等）,流媒体地址（http-flv,rtmp，rtsp等等）
     * @param width 图像宽度
     * @param height 图像高度
     * @param mode 模式（1-覆盖模式，0-连续截图，根据文件名称模板顺序生成）
     */
//    public static void record(String input,String output,Integer width,Integer height,String mode) throws Exception, org.bytedeco.javacv.FrameGrabber.Exception{
//
//    }


    public ImageRecorderService(String input, Integer width, Integer height, String mode, String monitorName){

        String stick = ProcessUtil.IS_WINDOWS?"\\":"/";
        this.input = input;
        this.output = url  + monitorName + stick +"%Y-%m-%d_%H-%M-%S.jpg";
        this.monitorName = monitorName;
        this.width = width;
        this.height = height;
        this.mode = mode;
        this.PARENT_DIR = url  + monitorName;
    }
    @SneakyThrows
    @Override
    public void run() {
        try{
            File localPath1 = new File(PARENT_DIR);
            if (!localPath1.exists()) {  // 获得文件目录，判断目录是否存在，不存在就新建一个
                localPath1.mkdirs();
            }
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
            //虽然rtsp本身是协议，但是对于ffmpeg来说，rtsp只是个多路复用器/解复用器。可以支持普通的rtp传输，也可以支持RDT传输的Real-RTSP协议
            grabber.setFormat("rtsp");
            //设置要从服务器接受的媒体类型，为空默认支持所有媒体类型，支持的媒体类型：[video，audio，data]
            grabber.setOption("allowed_media_types", "video");
            //设置RTSP传输协议为tcp传输模式
            grabber.setOption("rtsp_transport", "tcp");
            /*
             * rtsp_flags:[filter_src,prefer_tcp,listen]
             * filter_src:仅接受来自协商对等地址和端口的数据包。
             * prefer_tcp:如果TCP可用作RTSP RTP传输，请首先尝试使用TCP进行RTP传输。
             * listen:充当rtsp服务器，监听rtsp连接
             * rtp传输首选使用tcp传输模式
             */
            grabber.setOption("rtsp_flags", "prefer_tcp");
            //socket网络超时时间
            grabber.setOption("stimeout","3000000");
            //设置要缓冲以处理重新排序的数据包的数据包数量
//			grabber.setOption("reorder_queue_size","");
            //设置本地最小的UDP端口，默认为5000端口。
//			grabber.setOption("min_port","5000");
            //设置本地最大的UDP端口，默认为65000端口。
//			grabber.setOption("max_port","65000");
            grabber.start();

            if(width==null||height==null) {
                width=grabber.getImageWidth();
                height=grabber.getImageHeight();
            }

            FFmpegFrameRecorder recorder =new FFmpegFrameRecorder(output,width,height,0);

            //过滤掉日志
            avutil.av_log_set_level(avutil.AV_LOG_ERROR);

            recorder.setFormat("image2");
            if(mode==null) {
                mode="0";//默认连续截图
            }
            recorder.setOption("update", mode);
            recorder.setOption("strftime", "1"); //根据日期生成文件名
            try {
                recorder.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

//        CanvasFrame canvas = new CanvasFrame("图像预览");// 新建一个窗口
//        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Frame frame = null;

            // 只抓取图像画面
            for (;(frame = grabber.grabImage()) != null;) {
                try {

//                显示画面
//                canvas.showImage(frame);
                    //录制/推流
                    recorder.record(frame);
//                Thread.sleep(1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            recorder.close();//close包含stop和release方法。录制文件必须保证最后执行stop()方法，才能保证文件头写入完整，否则文件损坏。
            grabber.close();//close包含stop和release方法
        }catch (Exception e){
            log.error("视频快照失败！",e);
        }

    }

    public void saveCover() throws Exception, FrameGrabber.Exception {

            String coverPath = Constants.IMGROOTPATH+"cover/";
            File localPath1 = new File(coverPath);
            if (!localPath1.exists()) {  // 获得文件目录，判断目录是否存在，不存在就新建一个
                localPath1.mkdirs();
            }
            FFmpegFrameGrabber grabber = new FFmpegFrameGrabber(input);
            //虽然rtsp本身是协议，但是对于ffmpeg来说，rtsp只是个多路复用器/解复用器。可以支持普通的rtp传输，也可以支持RDT传输的Real-RTSP协议
            grabber.setFormat("rtsp");
            //设置要从服务器接受的媒体类型，为空默认支持所有媒体类型，支持的媒体类型：[video，audio，data]
            grabber.setOption("allowed_media_types", "video");
            //设置RTSP传输协议为tcp传输模式
            grabber.setOption("rtsp_transport", "tcp");
            /*
             * rtsp_flags:[filter_src,prefer_tcp,listen]
             * filter_src:仅接受来自协商对等地址和端口的数据包。
             * prefer_tcp:如果TCP可用作RTSP RTP传输，请首先尝试使用TCP进行RTP传输。
             * listen:充当rtsp服务器，监听rtsp连接
             * rtp传输首选使用tcp传输模式
             */
            grabber.setOption("rtsp_flags", "prefer_tcp");
            //socket网络超时时间
            grabber.setOption("stimeout","3000000");
            //设置要缓冲以处理重新排序的数据包的数据包数量
//			grabber.setOption("reorder_queue_size","");
            //设置本地最小的UDP端口，默认为5000端口。
//			grabber.setOption("min_port","5000");
            //设置本地最大的UDP端口，默认为65000端口。
//			grabber.setOption("max_port","65000");
            grabber.start();

            if(width==null||height==null) {
                width=grabber.getImageWidth();
                height=grabber.getImageHeight();
            }

            FFmpegFrameRecorder recorder =new FFmpegFrameRecorder(coverPath+monitorName+".png",width,height,0);

            //过滤掉日志
            avutil.av_log_set_level(avutil.AV_LOG_ERROR);

            recorder.setFormat("image2");
            if(mode==null) {
                mode="0";//默认连续截图
            }
            recorder.setOption("update", mode);
            recorder.setOption("strftime", "1"); //根据日期生成文件名
            try {
                recorder.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

//        CanvasFrame canvas = new CanvasFrame("图像预览");// 新建一个窗口
//        canvas.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Frame frame = null;

            // 只抓取图像画面
            for (;(frame = grabber.grabImage()) != null;) {
                try {

//                显示画面
//                canvas.showImage(frame);
                    //录制/推流
                    recorder.record(frame);
//                Thread.sleep(1000);
                    break;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            recorder.close();//close包含stop和release方法。录制文件必须保证最后执行stop()方法，才能保证文件头写入完整，否则文件损坏。
            grabber.close();//close包含stop和release方法

    }




    /** 视频播放指令执行 rtsp地址【源播放地址】通过ffmpeg程序进行转化成rtmp，将地址传给flv.js进行播放 */
//    public static Process videoPreview(String videoPath, String stream) throws NoSuchFieldException, IllegalAccessException {
//        CommandUtil commandUtil = new CommandUtil();
//        Process process = null;
//        int pid = -1;
//        /*如果是winodws系统**/
//        if (IS_WINDOWS) {
//            String cmd ="cmd /k start ffmpeg -rtsp_transport tcp -i"
//                    + " "
//                    + videoPath
//                    + " -vcodec libx264 -r 25 -preset ultrafast -tune zerolatency -f flv -an rtmp://localhost:1935/myapp/"
//                    + stream;
//            log.info(cmd);
//            process = commandUtil.winExec(cmd);
//            pid = getProcessIdInWindows(process);
//        }
//        /*如果是Linux系统**/
//        if (IS_LINUX){
//            System.out.println("linux");
//            /*String cmd = "nohup ffmpeg -nostdin -rtsp_transport tcp -i '"
//                    + ""
//                    + videoPath
//                    + "'"
//                    + " "
//                    + "-vcodec libx264 -r 25 -preset ultrafast -tune zerolatency -f flv -an rtmp://localhost:1935/myapp/"
//                    + stream
//                    + " >> /dev/null 2>&1 </dev/null &";*/
//            String[] cmd = {"ffmpeg","-rtsp_transport","tcp","-i",videoPath,"-vcodec","libx264","-r","25","-preset","ultrafast","-tune","zerolatency","-f","flv","-an","rtmp://localhost:1935/myapp/"+stream};
//            process = commandUtil.linuxExec(cmd);
//            pid = getProcessIdInLinux(process);
//        }
//        if(process!=null) {
//            log.info("启动推流进程成功，进程：" + pid);
//        }else{
//            log.warn("启动推流进程error");
//        }
//        return process;
//    }


    /** 关闭ffmpeg进程*/
    public static String close(int pid) {
        //这里需要停止特定进程
        log.info("停止推流进程，进程"+pid);
        String cmd = "taskkill /f /t /pid " + pid;
        try {
            log.info("执行命令:"+cmd);
            Runtime.getRuntime().exec(cmd);
            log.info("成功停止进程："+pid);
            return "已成功停止";
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.warn("停止进程"+pid+"失败！");
        return "杀死进程失败";
    }

}

