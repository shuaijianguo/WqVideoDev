package com.wq.controller;

import com.wq.pojo.Users;
import com.wq.pojo.vo.UsersVO;
import com.wq.service.UserService;
import com.wq.utils.JSONResult;
import com.wq.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.nio.ch.IOUtil;

import java.io.*;
import java.util.UUID;

/**
 * Created by wuqingvika on 2018/7/5.
 */
@RestController

@Api(value = "用户相关业务的接口", tags = {"用户相关业务的UserController"})
@RequestMapping("/user")
public class UserController extends BasicController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
            dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws Exception {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户Id不可为空");
        }
        String fileSpace = "D:/wqlesson/userData";//文件保存的命名空间
        String uploadPathDB = "/" + userId + "/face";//保存到数据库中的相对路径
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            if (files != null && files.length > 0) {
                String fileName = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(fileName)) {
                    //文件上传的最终保存路径
                    String finalFacePath = fileSpace + uploadPathDB + "/" + fileName;
                    //设置数据库保存的路径
                    uploadPathDB += ("/" + fileName);
                    File outFile = new File(finalFacePath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        //创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }
                    outputStream = new FileOutputStream(outFile);
                    inputStream = files[0].getInputStream();
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
        Users users = new Users();
        users.setId(userId);
        users.setFaceImage(uploadPathDB);///180425CFA4RB6T0H/face/wxa2049f5aead89372.o6zAJs5sm5bPFcTzKXp_5wXsWuso.W0eLNdT6MIvD3ba01f74ba779caa63d038c3c8200b4a.jpg
        userService.updateUserInfo(users);
        return JSONResult.ok(uploadPathDB);
    }

    @ApiOperation(value = "用户查询个人信息", notes = "用户查询个人信息的接口")
    @ApiImplicitParam(name = "userId", value = "用户Id", required = true,
            dataType = "String", paramType = "query")
    @PostMapping("/query")
    public JSONResult query(String userId)  {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户Id不可为空");
        }
        Users users = userService.queryUserByUserId(userId);
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(users,usersVO);
        return JSONResult.ok(usersVO);
    }

}
