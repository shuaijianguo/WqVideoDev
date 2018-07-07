package com.wq.controller;

import com.wq.service.BgmService;
import com.wq.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wuqingvika on 2018/7/7.
 */
@RequestMapping("/bgm")
@RestController
@Api(value = "背景音乐的接口", tags = {"背景音乐相关业务的BgmController"})

public class BgmController {

    @Autowired
    private BgmService bgmService;

    @PostMapping("/list")
    @ApiOperation(value = "查询背景音乐", notes = "查询背景音乐的接口")
    public JSONResult queryBgmList(){
        return JSONResult.ok(bgmService.queryBgmList());
    }
}
