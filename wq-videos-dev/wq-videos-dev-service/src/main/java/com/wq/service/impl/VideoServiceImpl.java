package com.wq.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wq.mapper.VideosMapper;
import com.wq.mapper.VideosMapperCustom;
import com.wq.pojo.Videos;
import com.wq.pojo.vo.VideosVO;
import com.wq.service.VideoService;
import com.wq.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wuqingvika on 2018/7/8.
 */
@Service
public class VideoServiceImpl implements VideoService{
    @Autowired
    private VideosMapper videosMapper;
    @Autowired
    private VideosMapperCustom videosMapperCustom;
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
    @Transactional(propagation = Propagation.REQUIRED)
    public void updateCoverVideoById(String id, String coverPath) {
        Videos videos=new Videos();
        videos.setId(id);
        videos.setCoverPath(coverPath);
        videosMapper.updateByPrimaryKeySelective(videos);//用了selective只更新实体类不为空的字段
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public PagedResult getAllVideos(Integer currentPage,Integer pageSize){
        PageHelper .startPage(currentPage,pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideosVO();
        PageInfo<VideosVO> pageInfo=new PageInfo<>(list);
        //装载需要返回的对象
        PagedResult pagedResult=new PagedResult();
        pagedResult.setPage(currentPage);
        pagedResult.setTotal(pageInfo.getPages());//总页数
        pagedResult.setRows(list);
        pagedResult.setRecords(pageInfo.getTotal());
        return pagedResult;
    }
}
