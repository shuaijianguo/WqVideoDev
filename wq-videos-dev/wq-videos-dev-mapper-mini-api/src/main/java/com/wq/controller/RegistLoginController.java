package com.wq.controller;

import com.wq.pojo.Users;
import com.wq.service.UserService;
import com.wq.utils.JSONResult;
import com.wq.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wuqingvika on 2018/7/1.
 */
@RestController

@Api(value = "用户注册/登录的接口",tags = {"注册/登录的controller"})
public class RegistLoginController {
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册",notes = "用户注册的接口")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody Users users) throws Exception{
        //1.判断用户名密码不为空
        if(StringUtils.isBlank(users.getUsername())||StringUtils.isBlank(users.getPassword())){
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户是否存在
        boolean isExist = userService.queryUsernameIsExist(users.getUsername());
        //3.注册新用户
        if(!isExist){
            users.setNickname(users.getUsername());
            users.setPassword(MD5Utils.getMD5Str(users.getPassword()));
            users.setFansCounts(0);
            users.setFollowCounts(0);
            users.setReceiveLikeCounts(0);
            userService.saveUser(users);
        }else{
            return JSONResult.errorMsg("用户名已存在");
        }
        return JSONResult.ok();
    }
}
