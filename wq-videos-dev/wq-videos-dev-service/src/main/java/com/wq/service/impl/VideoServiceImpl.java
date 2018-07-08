package com.wq.service.impl;

import com.wq.mapper.VideosMapper;
import com.wq.pojo.Videos;
import com.wq.service.VideoService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wuqingvika on 2018/7/8.
 */
@Service
public class VideoServiceImpl implements VideoService{
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private Sid sid;
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public String saveVideo(Videos video) {
        String id=sid.nextShort();
        video.setId(id);
        videosMapper.insertSelective(video);
        return id;
    }

    @Override
    public void updateCoverVideoById(String id, String coverPath) {
        Videos videos=new Videos();
        videos.setId(id);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);//用了selective只更新实体类不为空的字段
    }
}
