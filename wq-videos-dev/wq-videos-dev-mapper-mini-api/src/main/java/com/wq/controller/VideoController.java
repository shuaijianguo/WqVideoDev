package com.wq.controller;

import com.wq.pojo.Users;
import com.wq.service.BgmService;
import com.wq.utils.JSONResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by wuqingvika on 2018/7/7.
 */
@RequestMapping("/video")
@RestController
@Api(value = "视频相关的接口", tags = {"视频相关业务的VideoController"})

public class VideoController {

    @Autowired
    private BgmService bgmService;

    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐Id", required = false,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐播放长度", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
            dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "desc", value = "视频描述", required = false,
                    dataType = "String", paramType = "query")
    })
    @PostMapping(value = "/upload",headers = "content-type=multipart/form-data")
    public JSONResult upload(String userId, String bgmId,double videoSeconds,
                             int videoWidth,int videoHeight,String desc,
                             @ApiParam(value = "短视频",required = true) MultipartFile file) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户Id不可为空");
        }
        String fileSpace = "D:/wqlesson/userData";//文件保存的命名空间
        String uploadPathDB = "/" + userId + "/video";//保存到数据库中的相对路径
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            if (file != null ) {
                String fileName = file.getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    //文件上传的最终保存路径
                    String finalVideoPath = fileSpace + uploadPathDB + "/" + fileName;
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

        return JSONResult.ok();
    }

}
