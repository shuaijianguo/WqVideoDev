package com.wq.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuqingvika on 2018/7/7.
 */
public class FetchVideoCover {
    private String ffmpegExe;

    public FetchVideoCover(String ffmpegExe) {
        this.ffmpegExe = ffmpegExe;
    }

    public void convorter(String inputVideoPath,String coverOutputPath){
      //ffmpeg.exe -ss 00:00:01 -i test.mp4 -vframes 1 bb.jpg
        List<String> command=new ArrayList<>();
        command.add(ffmpegExe);
        command.add("-ss");
        command.add("00:00:01");
        command.add("-y");
        command.add("-i");//input缩写
        command.add(inputVideoPath);
        command.add("-vframes");
        command.add("1");
        command.add(coverOutputPath);
       /* for (String c:command) {
            System.out.print(c+" ");
        }*/
        ProcessBuilder builder=new ProcessBuilder(command);
        try {
            Process process=builder.start();
            InputStream errorStream = process.getErrorStream();
            InputStreamReader inputStreamReader=new InputStreamReader(errorStream);
            BufferedReader br=new BufferedReader(inputStreamReader);
            String line="";
            while((line=br.readLine())!=null){

            }
            if(br!=null){
                br.close();
            }
            if(inputStreamReader!=null){
                inputStreamReader.close();
            }
            if(errorStream!=null){
                errorStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void main(String[] args) {
        FetchVideoCover ffMpegTest=new FetchVideoCover("D:\\wqsoftware\\ffmpeg\\ffmpeg\\bin\\ffmpeg.exe");
        ffMpegTest.convorter("C:\\Users\\Administrator\\Desktop\\wq\\QQ视频20180710195530.mp4","C:\\Users\\Administrator\\Desktop\\wq\\cover.jpg");
    }
}
