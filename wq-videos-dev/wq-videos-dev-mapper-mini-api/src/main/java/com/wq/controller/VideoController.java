package com.wq.controller;

import com.wq.enums.VideoStatusEnum;
import com.wq.pojo.Bgm;
import com.wq.pojo.Videos;
import com.wq.service.BgmService;
import com.wq.service.VideoService;
import com.wq.utils.FetchVideoCover;
import com.wq.utils.JSONResult;
import com.wq.utils.MergeVideoMp3;
import com.wq.utils.PagedResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * Created by wuqingvika on 2018/7/7.
 */
@RequestMapping("/video")
@RestController
@Api(value = "视频相关的接口", tags = {"视频相关业务的VideoController"})

public class VideoController extends BasicController{

    @Autowired
    private BgmService bgmService;
    @Autowired
    private VideoService videoService;
    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐Id", required = false,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
            dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data")
    public JSONResult upload(String userId, String bgmId,double videoSeconds,
                             int videoWidth,int videoHeight,String desc,
                             @ApiParam(value = "短视频",required = true) MultipartFile file) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户Id不可为空");
        }
        String uploadPathDB = "/" + userId + "/video";//保存到数据库中的相对路径
        String coverPathDB="/"+userId+"/video";
        String finalVideoPath ="";
        //String finalVideoCover="";//最终的封面路径
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            if (file != null ) {
                String fileName = file.getOriginalFilename();
                /*String fileNameCopy=fileName.substring(0,fileName.lastIndexOf("."));
                String fileNamePrefix = fileName.substring(fileNameCopy.lastIndexOf(".")+1, fileName.lastIndexOf("."));
                */
                String fileNamePrefix= fileName.split("\\.")[fileName.split("\\.").length-2];
                //String fileNamePrefix=fileName.split("\\.")[0];
                if (StringUtils.isNotBlank(fileName)) {
                    //文件上传的最终保存路径
                     finalVideoPath = FILE_SPACE+ uploadPathDB + "/" + fileName;
                    coverPathDB=coverPathDB+"/"+fileNamePrefix+".jpg";
                    //设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    outputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, outputStream);
                } else {
                    return JSONResult.errorMsg("上传出错..");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("上传出错..");
        } finally {
            //ctrl+alt+t 生成try/catch/finally 快捷键
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
        //判断bgmId是否为空
        if(StringUtils.isNotBlank(bgmId)){
            //不为空 调用音视频合并工具
            MergeVideoMp3 tool=new MergeVideoMp3(FFMPEG_EXE);
            //查询到背景音乐
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String bgmPath = bgm.getPath();
            //新的相对路径
            uploadPathDB="/" + userId + "/video" + "/"+UUID.randomUUID().toString()+".mp4";
            String inputVideoPath=finalVideoPath;//把旧的视频路径赋给inputVideoPath;
            finalVideoPath= FILE_SPACE+ uploadPathDB;
            tool.convorter(inputVideoPath,FILE_SPACE+bgmPath,
                    videoSeconds,finalVideoPath);
        }
        System.out.println("newVideoPath:  "+uploadPathDB);
        System.out.println("finalVideoPath:  "+finalVideoPath);

        //利用ffmpeg生成视频封面
        FetchVideoCover coverTool=new FetchVideoCover(FFMPEG_EXE);
        coverTool.convorter(finalVideoPath,FILE_SPACE+coverPathDB);
        //保存视频信息到数据库
        Videos video=new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoDesc(desc);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoSeconds((float)videoSeconds);
        video.setVideoPath(uploadPathDB);
        video.setStatus(VideoStatusEnum.SUCCESS.value);//设置播放状态
        video.setCreateTime(new Date());
        video.setCoverPath(coverPathDB);
        String id = videoService.saveVideo(video);
        return JSONResult.ok(id);
    }

    @ApiOperation(value = "上传视频封面", notes = "上传视频封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频Id", required = false,
                    dataType = "String", paramType = "form")

    })
    @PostMapping(value = "/uploadCover",headers = "content-type=multipart/form-data")
    public JSONResult uploadCover(String userId, String videoId,
                             @ApiParam(value = "视频封面",required = true) MultipartFile file) throws Exception {
        if (StringUtils.isBlank(userId)||StringUtils.isBlank(videoId)) {
            return JSONResult.errorMsg("用户Id和视频Id不可为空");
        }
        String uploadPathDB = "/" + userId + "/video";//保存到数据库中的相对路径
        String finalCoverPath ="";
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            if (file != null ) {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    //文件上传的最终保存路径
                    finalCoverPath = FILE_SPACE+ uploadPathDB + "/" + fileName;
                    //设置数据库保存的相对路径
                    uploadPathDB += ("/" + fileName);
                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    outputStream = new FileOutputStream(outFile);
                    inputStream = file.getInputStream();
                    IOUtils.copy(inputStream, outputStream);
                } else {
                    return JSONResult.errorMsg("上传出错..");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return JSONResult.errorMsg("上传出错..");
        } finally {
            //ctrl+alt+t 生成try/catch/finally 快捷键
            if (outputStream != null) {
                outputStream.flush();
                outputStream.close();
            }
        }
        videoService.updateCoverVideoById(videoId,uploadPathDB);
        return JSONResult.ok();
    }

    /**
     *
     * @Description: 分页和搜索查询视频列表
     * isSaveRecord：1 - 需要保存
     * 				 0 - 不需要保存 ，或者为空的时候
     */
    @PostMapping(value="/showAll")
    public JSONResult showAll(@RequestBody Videos videos, Integer isSaveRecord, Integer page) throws Exception {
        if (page == null) {
            page = 1;
        }
        Integer pageSize=PAGE_SIZE;
        PagedResult result = videoService.getAllVideos(videos,isSaveRecord,page,pageSize);
        return JSONResult.ok(result);
    }

    @PostMapping(value="/hot")
    public JSONResult hot() throws Exception {
        return JSONResult.ok(videoService.getHotWords());
    }
}
