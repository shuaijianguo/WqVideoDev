package com.wq.controller;

import com.wq.pojo.Users;
import com.wq.pojo.vo.UsersVO;
import com.wq.service.UserService;
import com.wq.utils.JSONResult;
import com.wq.utils.MD5Utils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.catalina.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by wuqingvika on 2018/7/1.
 */
@RestController

@Api(value = "用户注册/登录的接口",tags = {"注册/登录的RegistLoginController"})
public class RegistLoginController extends BasicController{
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户注册",notes = "用户注册的接口")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody Users users) throws Exception{
        Thread.sleep(3000);
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
        users.setPassword("");
        UsersVO usersVO=setUserRedisSessionToken(users);
        return JSONResult.ok(usersVO);
    }

    public UsersVO setUserRedisSessionToken(Users userModel){
        String uniqueToken= UUID.randomUUID().toString();
        //加了冒号 redis会有层级分类
        redis.set(USER_REDIS_SESSION+":"+userModel.getId(),uniqueToken,1000*30*60);//30分钟
        UsersVO usersVO=new UsersVO();
        BeanUtils.copyProperties(userModel,usersVO);//拷到Vo中去
        usersVO.setUserToken(uniqueToken);
        return usersVO;
    }

    @ApiOperation(value = "用户登录",notes = "用户登录的接口")
    @PostMapping("/login")
    public JSONResult login(@RequestBody Users users) throws Exception{
        //1.判断用户名密码是否为空
        if(StringUtils.isBlank(users.getUsername())||StringUtils.isBlank(users.getPassword())){
            return JSONResult.errorMsg("用户名和密码不能为空");
        }
        //2.判断用户密码是否正确
        Users userResult = userService.queryUserByNameAndPwd(users.getUsername(),MD5Utils.getMD5Str(users.getPassword()));
        //3.存在该用户
        if(userResult!=null){
            userResult.setPassword("");//因为密码返回给前端的 所以要保密
            UsersVO usersVO=setUserRedisSessionToken(userResult);
            return JSONResult.ok(usersVO);
        }else{
            return JSONResult.errorMsg("用户名或密码不正确");
        }
    }

    @ApiOperation(value = "用户注销",notes = "用户注销的接口")
    @ApiImplicitParam(name = "userId",value = "用户Id",required = true,
            dataType = "String",paramType = "query")
    @PostMapping("/logout")
    public JSONResult logOut(String userId) throws Exception{
       redis.del(USER_REDIS_SESSION+":"+userId);
       return JSONResult.ok();
    }
}
