package com.wq.controller;

import com.wq.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wuqingvika on 2018/7/1.
 */
@RestController
public class BasicController {
    @Autowired
    public RedisOperator redis;
    public static final String USER_REDIS_SESSION="user-redis-session";
    public static final String FILE_SPACE="D:/wqlesson/userData";//存放文件保存的命名空间

    public static final String FFMPEG_EXE ="D:\\wqsoftware\\ffmpeg\\bin\\ffmpeg.exe";

}
