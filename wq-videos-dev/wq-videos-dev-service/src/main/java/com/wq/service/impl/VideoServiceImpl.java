package com.wq.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wq.mapper.*;
import com.wq.pojo.SearchRecords;
import com.wq.pojo.UsersLikeVideos;
import com.wq.pojo.Videos;
import com.wq.pojo.vo.VideosVO;
import com.wq.service.VideoService;
import com.wq.utils.PagedResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

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
    private SearchRecordsMapper searchRecordsMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;

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
    @Transactional(propagation = Propagation.REQUIRED)
    public PagedResult getAllVideos(Videos videos, Integer isSaveRecord,Integer currentPage,Integer pageSize){
       String videoDesc=videos.getVideoDesc();
        //判断是否需要保存热搜词
        if(isSaveRecord!=null&&isSaveRecord==1){
            //将热搜词保存至数据库
            String id = sid.nextShort();
            SearchRecords searchRecords=new SearchRecords();
            searchRecords.setId(id);
            searchRecords.setContent(videoDesc);
            searchRecordsMapper.insert(searchRecords);
        }

        PageHelper .startPage(currentPage,pageSize);
        List<VideosVO> list = videosMapperCustom.queryAllVideosVO(videoDesc);
        PageInfo<VideosVO> pageInfo=new PageInfo<>(list);
        //装载需要返回的对象
        PagedResult pagedResult=new PagedResult();
        pagedResult.setPage(currentPage);
        pagedResult.setTotal(pageInfo.getPages());//总页数
        pagedResult.setRows(list);
        pagedResult.setRecords(pageInfo.getTotal());
        return pagedResult;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<String> getHotWords() {
        return  searchRecordsMapper.getHotWords();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void likeVideo(String userId, String videoId, String createrId) {
        //1.视频获取的赞数+1
        videosMapperCustom.addVideoLike(videoId);
        //2.用户视频关联表添加记录
        UsersLikeVideos ulv=new UsersLikeVideos();
        String ulvId = sid.nextShort();
        ulv.setId(ulvId);
        ulv.setUserId(userId);
        ulv.setVideoId(videoId);
        usersLikeVideosMapper.insert(ulv);
        //3.视频作者收到点赞数+1
        usersMapper.addReceiveLikeCount(createrId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void unLikeVideo(String userId, String videoId, String createrId) {
        //1.视频获取的赞数-1
        videosMapperCustom.reduceVideoLike(videoId);
        //2.用户视频关联表删除记录
        Example userLikeVideoExample=new Example(UsersLikeVideos.class);
        Criteria criteria=userLikeVideoExample.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);//videoId注意要和pojo定义一致
        usersLikeVideosMapper.deleteByExample(userLikeVideoExample);
        //3.视频作者收到点赞数-1
        usersMapper.reduceReceiveLikeCount(createrId);
    }
}
