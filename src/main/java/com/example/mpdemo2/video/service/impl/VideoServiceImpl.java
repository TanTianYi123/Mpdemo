package com.example.mpdemo2.video.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.mpdemo2.base.FFmpegProcess;
import com.example.mpdemo2.carrecord.domain.CarRecords;
import com.example.mpdemo2.facerecord.domain.FaceRecords;
import com.example.mpdemo2.util.ProcessUtil;
import com.example.mpdemo2.video.Configuration.VideoConfiguration;
import com.example.mpdemo2.video.domain.FfmpegProcess;
import com.example.mpdemo2.video.domain.Video;
import com.example.mpdemo2.video.service.VideoService;
import com.example.mpdemo2.video.mapper.VideoMapper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.FrameRecorder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static com.example.mpdemo2.base.InitFFmpeg.ffmpegProcessMap;
import static com.example.mpdemo2.base.InitFFmpeg.imageRecorderMap;

/**
* @author Administrator
* @description 针对表【video】的数据库操作Service实现
* @createDate 2024-01-02 14:59:12
*/
@Service
@Slf4j
@MapperScan(value = "com.example.mpdemo2.data.video.mapper")
public class VideoServiceImpl extends ServiceImpl<VideoMapper, Video>
    implements VideoService{
    @Autowired
    private VideoMapper videoMapping;

    @Resource(name = "threadPoolTaskExecutor")
    public ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 存放rtsp地址和推拉流进程的映射关系
     */
    private static HashMap<String, FfmpegProcess> map = new HashMap<>();

    /**
     * 查询全部摄像头信息
     *
     * @return 摄像头list
     */
    public List<Video> selectAllVideos() {
        return videoMapping.selectAllVideos();
    }

    /**
     * 查询摄像头信息
     *
     * @param feature 模糊查询特征字符串
     * @return 分页查询结果
     */
    public List<Video> selectAllVideos(String feature) {
        List<Video> videos;
        if (feature != null && !feature.equals("")) {
            feature = "%" + feature + "%";
            videos = videoMapping.selectVideosByFeature(feature);
        } else {
            videos = videoMapping.selectAllVideos();
        }
        return videos;
    }

    public PageInfo<FaceRecords> selectFaceRecordsByPage(Integer pageSize, Integer pageNum) {
        List<FaceRecords> faceRecords = videoMapping.selectAllFaceRecords();

        return new PageInfo<>(faceRecords, 5);
    }

    public PageInfo<CarRecords> selectCarRecordsByPage(Integer pageSize, Integer pageNum) {
        List<CarRecords> carRecords = videoMapping.selectAllCarRecords();

        return new PageInfo<>(carRecords, 5);
    }

    public void deleteVideo(String rtsp) {
        videoMapping.deleteVideo(rtsp);

        synchronized (ffmpegProcessMap) {
            if (!ffmpegProcessMap.isEmpty()) {
                for (Video video : ffmpegProcessMap.keySet()) {
                    if (video.getRtsp().equals(rtsp)) {
                        //注销进程
                        FFmpegProcess p = ffmpegProcessMap.get(video);
                        p.destroy();
                        //删除map表
                        ffmpegProcessMap.remove(video);
                        imageRecorderMap.remove(video);
                        //查看该bean
                        //VideoRecorderService videoRecorderService = VideoConfiguration.getBean(VideoRecorderService.class,video.getMonitorName());
                        //System.out.println(videoRecorderService);

                        //注销bean
                        VideoConfiguration.unRegistryBean(video.getMonitorName());

                        log.info("摄像头进程关闭成功！");
                    }

                }
            } else {
//                log.info("暂无推流进程运行。");
            }
        }
    }


    /**
     * 使map中，stream参数对应的rtsp地址下的页面数量加一
     * 如果在数量减少后rtsp地址下没有页面，则销毁推流进程
     *
     * @param stream
     */
    public void dropSession(String stream) {
        String rtsp = videoMapping.selectRtspByStream(stream);
        synchronized (map) {
            FfmpegProcess ffmpegProcess = map.get(rtsp);
            ffmpegProcess.decrease();
            log.info("目前流" + rtsp + "下的页面还有：" + ffmpegProcess.getCount() + "个");
            //如果流已无人使用，关闭推流进程
            if (ffmpegProcess.getCount() <= 0) {
                System.out.println(ProcessUtil.close(map.get(rtsp).getPid()));
                map.remove(rtsp);
            }
        }

    }

    public int insertVideo(String monitorName,
                           String username,
                           String password,
                           String ip,
                           String port,
                           String description,
                           boolean personAi,
                           boolean carAi) {
        String rtsp = "rtsp://" + username + ":" + password + "@" + ip + ":" + port;
        String stream = "stream" + UUID.randomUUID();
        Video video = new Video(null, monitorName, rtsp, description, stream, personAi, carAi);
        int count = videoMapping.insertIntoVideo(video);
        if (count == 1) {
            VideoConfiguration.registerBean(VideoRecorderService.class, monitorName);
            try {
                FFmpegProcess process = ProcessUtil.videoPreview(video.getRtsp(), video.getStream());
                ffmpegProcessMap.put(video, process);

                ImageRecorderService imageRecorderService = new ImageRecorderService(video.getRtsp(), 1280, 720, "0", video.getMonitorName());
                imageRecorderService.saveCover();
                runExample(imageRecorderService.getPARENT_DIR(), video);
//            threadPoolTaskExecutor.execute(imageRecorderService);
                Future<?> call = threadPoolTaskExecutor.submit(imageRecorderService);
                imageRecorderMap.put(video, call);
            } catch (NoSuchFieldException | IllegalAccessException | FrameRecorder.Exception | FrameGrabber.Exception e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        return count;

    }

//    public void insertFaceRecord(FaceRecordsEntity faceRecord) {
//        videoMapping.insertFaceRecord(faceRecord);
//    }
//
//    public void insertCarRecord(CarRecordsEntity carRecord) {
//        videoMapping.insertCarRecord(carRecord);
//    }
//
//    public FaceRecordsEntity callFaceAI(File file, VideoEntity video) {
//        boolean personAI = video.isPersonAi();
//        boolean carAI = video.isCarAi();
//        if (!personAI && !carAI) return null;
//        /*double random = Math.random();
//        if (random<0.33)file=new File("/home/thg/0.jpg");
//        else if (random<0.67)file = new File("/home/thg/car.png");
//        else file = new File("/home/thg/carId.jpeg");*/
//        FaceRecordsEntity faceRecord = null;
//        CarRecordsEntity carRecord = null;
//        DataOutputStream out = null;
//        VideoRecorderService videoRecorderService = VideoConfiguration.getBean(VideoRecorderService.class, video.getMonitorName());
//        final String newLine = "\r\n";
//        final String prefix = "--";
//        try {
//            URL url = new URL("http://localhost:5000/ssd_predict");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            System.out.println("连接http://localhost:5000/ssd_predict");
//
//            String BOUNDARY = "-------7da2e536604c8";
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Charsert", "UTF-8");
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//            out = new DataOutputStream(conn.getOutputStream());
//            StringBuilder sb1 = new StringBuilder();
//            sb1.append(prefix);
//            sb1.append(BOUNDARY);
//            sb1.append(newLine);
//            sb1.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"" + newLine);
//            sb1.append("Content-Type:application/octet-stream");
//            sb1.append(newLine);
//            sb1.append(newLine);
//            out.write(sb1.toString().getBytes());
//            DataInputStream in = new DataInputStream(new FileInputStream(file));
//            byte[] bufferOut = new byte[1024];
//            int bytes = 0;
//            while ((bytes = in.read(bufferOut)) != -1) {
//                out.write(bufferOut, 0, bytes);
//            }
//            out.write(newLine.getBytes());
//            in.close();
//
//            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
//            out.write(end_data);
//            out.flush();
//            out.close();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = null;
//            line = reader.readLine();
//
//            reader.close();
//            JSONObject result = JSON.parseObject(line);
//            String msg = "";
////            boolean isPerson = result.getString("class_name").contains("person");
////            boolean isCar = result.getString("class_name").contains("car") || result.getString("class_name").contains("bus");
////            if (isPerson||isCar) {
//            if (true) {
//                log.info("算法：" + line.substring(0, Math.min(line.length(), 100)));
//
//                //判断有几个人和车以及最高概率
//                String[] classes = result.getString("class_name").split(";");
//                int personCount = 0;
//                int carCount = 0;
//                double personScore = 0;
//                double carScore = 0;
//                for (String s : classes) {
//                    //person
//                    if (personAI && s.contains("person")) {
//                        personCount++;
//                        double thisPersonScore = Double.parseDouble(s.substring(s.indexOf("person: ") + "person: ".length()));
//                        if (thisPersonScore > personScore) {
//                            personScore = thisPersonScore;
//                        }
//                    }
//
//                    //car
//                    if (carAI && s.contains("car")) {
//                        carCount++;
//                        double thisCarScore = Double.parseDouble(s.substring(s.indexOf("car: ") + "car: ".length()));
//                        if (thisCarScore > carScore) {
//                            carScore = thisCarScore;
//                        }
//                    }
//                    //bus
//                    if (carAI && s.contains("bus")) {
//                        carCount++;
//                        double thisCarScore = Double.parseDouble(s.substring(s.indexOf("bus: ") + "bus: ".length()));
//                        if (thisCarScore > carScore) {
//                            carScore = thisCarScore;
//                        }
//                    }
//
//                }
//                try (Jedis jedis = JedisConnectionFactory.getJedis()) {
//                    if (personScore > 0.6) {
//                        //三秒内均有识别到人再录制
//                        jedis.setex(video.getRtsp() + "2second", 3, "true");
//                        videoRecorderService.setStatus(true);
//
//                        //该视频没有正在录制并且识别出人的概率在0.3以上，开始录制
//                        if ( jedis.exists(video.getRtsp() + "2second")) {
//                            //睡眠两秒，防止第一张图片准备录制，但第二张图片判断时，第一张图片还没开始录制，只要延时的时间大于两张照片到达的间隔即可
//                            Thread.sleep(1000);
//                            if (!videoRecorderService.isRecording()){
//                                //录制视频
//                                log.info("监控点" + video.getMonitorName() + "开始录制...");
//                                videoRecorderService.startRecordVideo(video);
//                            }
//
//                        }
//                    } else {
//                        videoRecorderService.setStatus(false);
//                    }
//
//				/*msg = line.substring(line.indexOf("\"img_str\":\"")+"\"img_str\":\"".length(), line.indexOf("\"}"));
//				log.info(msg);*/
//
//                    //如果五分钟内没有进行该操作，则保存照片 + 插入数据库
//                    String fileName = DateUtil.fileFormat.format(new Date()) + ".png";
//
//                    if (personAI && (personScore > 0.6) && jedis.exists(video.getRtsp() + "2second")
//                             && (!jedis.exists(video.getMonitorName() + "_person"))) {
//                        BASE64Decoder decoder = new BASE64Decoder();
//                        // 解密
//                        byte[] b = decoder.decodeBuffer(result.getString("img_str"));
//                        // 处理数据
//                        for (int i = 0; i < b.length; ++i) {
//                            if (b[i] < 0) {
//                                b[i] += 256;
//                            }
//                        }
//                        String outpath = Constants.FACEIMGOUTPUTPATH + video.getMonitorName() + "/";
//                        File dir = new File(outpath);
//                        if (!dir.exists()) dir.mkdirs();
//                        OutputStream out2 = new FileOutputStream(outpath + fileName);
//                        out2.write(b);
//                        out2.flush();
//                        out2.close();
//                        log.info("监控点" + video.getMonitorName() + "有人，图片已存");
//                        faceRecord = new FaceRecordsEntity();
////						faceRecord.setResult(personCount + "个人");
//                        faceRecord.setResult("监控点：" + video.getMonitorName() + "，有人出现");
//                        faceRecord.setScore(personScore);
//                        faceRecord.setPictureUrl(video.getMonitorName() + "/" + fileName);
//                        insertFaceRecord(faceRecord);
//                        //五分钟内该监测点还有人就不开始新的录制
//                        jedis.setex(video.getMonitorName() + "_person", 5 * 60, "true");
//                    }
//                    if (carAI && (!jedis.exists(video.getMonitorName() + "_car")) && (carScore > 0.6)) {
//                        BASE64Decoder decoder = new BASE64Decoder();
//                        // 解密
//                        byte[] b = decoder.decodeBuffer(result.getString("img_str"));
//                        // 处理数据
//                        for (int i = 0; i < b.length; ++i) {
//                            if (b[i] < 0) {
//                                b[i] += 256;
//                            }
//                        }
//                        carRecord = callLicenseAI(file, video);
//                        if (carRecord == null) {
//                            String outpath = Constants.CARIMGOUTPUTPATH + video.getMonitorName() + "/";
//                            File dir = new File(outpath);
//                            if (!dir.exists()) dir.mkdirs();
//                            OutputStream out2 = new FileOutputStream(outpath + fileName);
//                            out2.write(b);
//                            out2.flush();
//                            out2.close();
//                            carRecord = new CarRecordsEntity();
//                            carRecord.setResult("监控点：" + video.getMonitorName() + carCount + "辆车");
//                            carRecord.setScore(carScore);
//                            carRecord.setPictureUrl(video.getMonitorName() + "/" + fileName);
//                        }
//                        log.info("监控点" + video.getMonitorName() + "有车，图片已存");
//                        insertCarRecord(carRecord);
//                        jedis.setex(video.getMonitorName() + "_car", 1 * 60, "true");
//                    }
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//
//        } catch (Exception e) {
//            log.error("发送POST请求出现异常！", e);
//        }
//        //删除照片文件
//
//        return faceRecord;
//    }
//
//
//    public static CarRecordsEntity callLicenseAI(File file, VideoEntity video) {
//
//        CarRecordsEntity carRecord = null;
//        DataOutputStream out = null;
//        final String newLine = "\r\n";
//        final String prefix = "--";
//        try {
//            URL url = new URL("http://localhost:5000/carID_image_predict");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//            String BOUNDARY = "-------7da2e536604c8";
//            conn.setRequestMethod("POST");
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Charsert", "UTF-8");
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//            out = new DataOutputStream(conn.getOutputStream());
//            StringBuilder sb1 = new StringBuilder();
//            sb1.append(prefix);
//            sb1.append(BOUNDARY);
//            sb1.append(newLine);
//            sb1.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"" + newLine);
//            sb1.append("Content-Type:application/octet-stream");
//            sb1.append(newLine);
//            sb1.append(newLine);
//            out.write(sb1.toString().getBytes());
//            DataInputStream in = new DataInputStream(new FileInputStream(file));
//            byte[] bufferOut = new byte[1024];
//            int bytes = 0;
//            while ((bytes = in.read(bufferOut)) != -1) {
//                out.write(bufferOut, 0, bytes);
//            }
//            out.write(newLine.getBytes());
//            in.close();
//
//            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
//            out.write(end_data);
//            out.flush();
//            out.close();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//            String line = null;
//            line = reader.readLine();
//
//            JSONObject result = JSON.parseObject(line);
//            String msg = "";
//            if (!ObjectUtil.isEmptyString(result.getString("class_name"))) {
//				/*msg = line.substring(line.indexOf("\"img_str\":\"")+"\"img_str\":\"".length(), line.indexOf("\"}"));
//				log.info(msg);*/
//                log.info("车牌识别算法：" + line.substring(0, 100));
//                carRecord = new CarRecordsEntity();
//                String[] results = result.getString("class_name").split("/n");
//                StringBuffer IDs = new StringBuffer();
//                double score = 0;
//                for (String s : results) {
//                    String[] oneResult = s.split(" ");
//                    IDs.append(oneResult[0] + " ");
//                    double thisScore = Double.parseDouble(oneResult[1]);
//                    if (thisScore > score) {
//                        score = thisScore;
//                    }
//                }
//                carRecord.setResult(IDs.toString());
//                carRecord.setScore(score);
//
//                BASE64Decoder decoder = new BASE64Decoder();
//                try {
//                    // 解密
//                    byte[] b = decoder.decodeBuffer(result.getString("img_str"));
//                    // 处理数据
//                    for (int i = 0; i < b.length; ++i) {
//                        if (b[i] < 0) {
//                            b[i] += 256;
//                        }
//                    }
//                    String fileName = DateUtil.fileFormat.format(new Date()) + ".png";
//                    String outpath = Constants.CARIMGOUTPUTPATH + video.getMonitorName() + "/";
//                    File dir = new File(outpath);
//                    if (!dir.exists()) dir.mkdirs();
//                    OutputStream out2 = new FileOutputStream(outpath + fileName);
//                    out2.write(b);
//                    out2.flush();
//                    out2.close();
//                    carRecord.setPictureUrl(video.getMonitorName() + "/" + fileName);
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//
//
//        } catch (Exception e) {
//            log.error("发送POST请求出现异常！", e);
//            e.printStackTrace();
//        }
//        return carRecord;
//    }

    public void runExample(String PARENT_DIR, Video video) throws Exception {

        File parentDir = FileUtils.getFile(PARENT_DIR);

        FileAlterationObserver observer = new FileAlterationObserver(parentDir);

        observer.addListener(new FileAlterationListenerAdaptor() {

            @Override
            public void onFileCreate(File file) {

//                callFaceAI(file, video);

//                if (faceResult!=null){
//                    try {
//                        videoMapping.insertFaceRecord(faceResult);
//                    }catch (java.lang.Exception e){
//                        e.printStackTrace();
//                    }
//                }
                /*CarRecordsEntity carResult = LicenseController.callLicenseAI(file);
                if (carResult!=null){
                    try {
                        videoMapping.insertCarRecord(carResult);
                    }catch (java.lang.Exception e){
                        e.printStackTrace();
                    }
                }*/
//                System.out.println("算法识别" + file.getName());
            }

            @Override
            public void onFileDelete(File file) {
//                log.info("File deleted: " + file.getName());
            }

            @Override
            public void onDirectoryCreate(File dir) {
                System.out.println("Directory created: " + dir.getName());
            }

            @Override
            public void onDirectoryDelete(File dir) {
                System.out.println("Directory deleted: " + dir.getName());
            }
        });

        // 轮询间隔
        long interval = TimeUnit.SECONDS.toMillis(1);//将一秒转换为毫秒数
        //创建文件变化监听器
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);

        monitor.start();
    }

}




