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
public class MergeVideoMp3 {
    private String ffmpegExe;

    public MergeVideoMp3(String ffmpegExe) {
        this.ffmpegExe = ffmpegExe;
    }

    public void convorter(String inputVideoPath,String mp3InputPath,double seconds,String outputVideoPath){
      // 将mp3合到视频中生成一个新的视频文件
        List<String> command=new ArrayList<>();
        command.add(ffmpegExe);
        command.add("-i");//input缩写
        command.add(inputVideoPath);
        command.add("-i");//input缩写
        command.add(mp3InputPath);
        command.add("-t");//time缩写 这个时间用的是视频的时间 音频的时间以视频时长为基准
        command.add(String.valueOf(seconds));
        command.add("-y");//覆盖替换的意思
        command.add(outputVideoPath);
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
        MergeVideoMp3 ffMpegTest=new MergeVideoMp3("D:\\wqsoftware\\ffmpeg\\bin\\ffmpeg.exe");
        ffMpegTest.convorter("C:\\Users\\wuqingvika\\Desktop\\musicwq\\test.mp4","C:\\Users\\wuqingvika\\Desktop\\musicwq\\wqmusic.mp3",9.1,"C:\\Users\\wuqingvika\\Desktop\\musicwq\\吴里庆庆拍的小书架.mp4");
    }
}
