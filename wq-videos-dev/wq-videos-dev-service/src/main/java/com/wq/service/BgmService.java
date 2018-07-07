package com.wq.service;

import com.wq.pojo.Bgm;
import com.wq.pojo.Users;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import java.util.List;

/**
 * Created by wuqingvika on 2018/7/7.
 */
public interface BgmService {
    /**
     * 查询背景音乐列表
     * @return
     */
    public List<Bgm> queryBgmList();
}
