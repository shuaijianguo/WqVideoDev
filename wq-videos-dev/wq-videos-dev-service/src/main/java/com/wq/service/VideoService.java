package com.wq.service;

import com.wq.pojo.Bgm;
import com.wq.pojo.Videos;

import java.util.List;

/**
 * Created by wuqingvika on 2018/7/7.
 */
public interface VideoService {
    /**
     * 保存视频
     * @param videos
     */
    public String saveVideo(Videos videos);

    /**
     * 修改视频为其设置封面
     * @param id
     * @param coverPath
     */
    public void updateCoverVideoById(String id,String coverPath);
}
