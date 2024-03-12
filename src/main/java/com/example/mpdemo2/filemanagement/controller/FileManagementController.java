package com.example.mpdemo2.filemanagement.controller;

import com.example.mpdemo2.filemanagement.entity.AIOFile;
import com.example.mpdemo2.util.Constants;
import com.example.mpdemo2.util.ObjectUtil;
import com.example.mpdemo2.util.ProcessUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class FileManagementController {
    private static final String dataDir = Constants.DataROOTPATH;
    private static final String videoDir = Constants.VIDEOPATH;
    private static final String logsDir = ProcessUtil.IS_WINDOWS ? "D:\\work\\CET\\log\\" : "/home/user/CET/log";
    @RequestMapping("/fileManagement")
    public String toFileManagementPage(HttpServletRequest request) throws IOException {
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        List<AIOFile> videoFileList = new ArrayList<>();
        double videoTotalSize = getAllFileInformationInDir(new File(videoDir), videoFileList) / (1024.0 * 1024.0);
        List<AIOFile> dataFileList = new ArrayList<>();
        double dataTotalSize = getAllFileInformationInDir(new File(dataDir), dataFileList) / (1024.0 * 1024.0);
        List<AIOFile> logsFileList = new ArrayList<>();
        double logsTotalSize = getAllFileInformationInDir(new File(logsDir), logsFileList) / (1024.0 * 1024.0);
        request.setAttribute("videoTotalSize", decimalFormat.format(videoTotalSize));
        request.setAttribute("dataTotalSize", decimalFormat.format(dataTotalSize));
        request.setAttribute("logsTotalSize", decimalFormat.format(logsTotalSize));
        request.setAttribute("videoFileList", videoFileList);
        request.setAttribute("dataFileList", dataFileList);
        request.setAttribute("logsFileList", logsFileList);
        request.setAttribute("class","video");
        log.info("进入文件管理成功！");
        return "fileManagement";
    }
    private long getAllFileInformationInDir(final File file, List<AIOFile> fileList) throws IOException {
        if (file.isFile()) {
            fileList.add(getFileInformation(file));

            return file.length();
        }
        final File[] children = file.listFiles();
        long total = 0;
        if (children != null) {
            for (final File child : children) {
                total += getAllFileInformationInDir(child, fileList);
            }
        }
        return total;
    }

    private AIOFile getFileInformation(final File file) throws IOException {
        if (!file.isFile()) {
            throw new RuntimeException("不是文件");
        }
        return new AIOFile(file.getName(), file.getAbsolutePath(), file);
    }
    @RequestMapping("/download")
    public void download(String fileName, String path, HttpServletResponse resp) throws Exception {
        try {
            File file = new File(path);
            //如果文件不存在
            if (!file.exists()) {
                log.error("下载文件" + fileName + "不存在");
            }
            //解决下载文件时文件名乱码问题
            byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
            fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);
            resp.reset();
            resp.setContentType("application/octet-stream");
            resp.setCharacterEncoding("utf-8");
            resp.setContentLength((int) file.length());
            //设置响应头，控制浏览器下载该文件
            resp.setHeader("content-disposition", "attachment;filename=" + fileName);
            try {
                //读取要下载的文件，保存到文件输入流
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
                //创建输出流
                OutputStream os = resp.getOutputStream();
                //缓存区
                byte[] buff = new byte[1024];
                int i = 0;
                //循环将输入流中的内容读取到缓冲区中
                while ((i = bis.read(buff)) != -1) {
                    os.write(buff, 0, i);
                    os.flush();
                }
                //关闭
                bis.close();
                os.close();
            } catch (IOException e) {
                log.error("文件" + file.getName() + "下载失败，{}", e);
                log.info("文件" + file.getName() + "下载失败");
            }
            log.info("文件" + file.getName() + "下载成功");
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
    }

    @RequestMapping("/deleteOneFile")
    public String deleteOneFile(String fileName, String path, HttpServletRequest request) throws IOException {
        try{
            File file = new File(path);
            // 如果文件路径只有单个文件
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    int cnt = deleteEmptyDirs(file.getParentFile(),true);
                    String message = "删除文件" + fileName + "成功！";
                    if (cnt<0)message += "但空文件夹删除失败！";
                    log.info(message);
                    request.setAttribute(Constants.MSG, message);
                    toFileManagementPage(request);
                } else {
                    request.setAttribute(Constants.MSG, "删除文件" + fileName + "失败！");
                    log.error("删除文件" + fileName + "失败！");
                    toFileManagementPage(request);
                }
            } else {
                request.setAttribute(Constants.MSG, fileName + "不存在！");
                log.error(fileName + "不存在！");

            }
        } catch (Exception e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
            toFileManagementPage(request);
        }finally {
            return toFileManagementPage(request);
        }
    }

    /**
     * 递归删除空目录，最多删除两层目录
     * @param file 目录
     * @param r 是否递归
     * @return 删除的文件夹目录数
     */
    private static int deleteEmptyDirs(File file,boolean r){
        if (!file.exists()||file.isFile())return -1;
        int cnt = 0;
        if (file.isDirectory()){
            if (file.listFiles().length>0)return 0;
            cnt = file.delete()?cnt+1:cnt;
            if (r){
                File parentFile = file.getParentFile();
                cnt+=deleteEmptyDirs(parentFile,false);
            }
        }
        return cnt;
    }
    @RequestMapping("/returnFile")
    public String returnFile(HttpServletRequest request, String feature) throws IOException {
        String kind =request.getParameter("class");

        if (ObjectUtil.isEmptyString(kind))kind="video";
        request.setAttribute("class",kind);
        DecimalFormat decimalFormat = new DecimalFormat("###0.00");
        List<AIOFile> videoFileList = new ArrayList<>();
        double videoTotalSize = getAllFileInformationInDir(new File(videoDir), videoFileList) / (1024.0 * 1024.0);
        List<AIOFile> dataFileList = new ArrayList<>();
        double dataTotalSize = getAllFileInformationInDir(new File(dataDir), dataFileList) / (1024.0 * 1024.0);
        List<AIOFile> logsFileList = new ArrayList<>();
        double logsTotalSize = getAllFileInformationInDir(new File(logsDir), logsFileList) / (1024.0 * 1024.0);

        List<AIOFile> videoList = new ArrayList<>();
        List<AIOFile> dataList = new ArrayList<>();
        List<AIOFile> logsList = new ArrayList<>();

        for(AIOFile file : videoFileList){
            if(file.getFileName().contains(feature) || file.getCreatedTime().toString().contains(feature)){
                videoList.add(file);
            }
        }
        for(AIOFile file : dataFileList){
            if(file.getFileName().contains(feature) || file.getCreatedTime().toString().contains(feature)){
                dataList.add(file);
            }
        }   for(AIOFile file : logsFileList){
            if(file.getFileName().contains(feature) || file.getCreatedTime().toString().contains(feature)){
                logsList.add(file);
            }
        }
        request.setAttribute("videoTotalSize", decimalFormat.format(videoTotalSize));
        request.setAttribute("dataTotalSize", decimalFormat.format(dataTotalSize));
        request.setAttribute("logsTotalSize", decimalFormat.format(logsTotalSize));
        request.setAttribute("videoFileList", videoList);
        request.setAttribute("dataFileList", dataList);
        request.setAttribute("logsFileList", logsList);
        log.info("进入文件管理成功！");
        return "fileManagement";

    }

}
