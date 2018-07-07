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
public class FFMpegTest {
    private String ffmpegExe;

    public FFMpegTest(String ffmpegExe) {
        this.ffmpegExe = ffmpegExe;
    }

    public void convorter(String inputVideoPath,String outputVideoPath){
      // 一句命令搞定: ffmpeg -i input.mp4 output.avi
        List<String> command=new ArrayList<>();
        command.add(ffmpegExe);
        command.add("-i");//input缩写
        command.add(inputVideoPath);
        command.add("-y");//替换的意思
        command.add(outputVideoPath);
        for (String c:command) {
            System.out.print(c+" ");
        }
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
        FFMpegTest ffMpegTest=new FFMpegTest("D:\\wqsoftware\\ffmpeg\\bin\\ffmpeg.exe");
        ffMpegTest.convorter("C:\\Users\\wuqingvika\\Desktop\\musicwq\\yangmi.mp4","C:\\Users\\wuqingvika\\Desktop\\musicwq\\iloveym.avi");
    }
}
