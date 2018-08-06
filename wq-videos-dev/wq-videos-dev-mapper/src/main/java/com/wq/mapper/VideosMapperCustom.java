package com.wq.mapper;

import com.wq.pojo.Videos;
import com.wq.pojo.vo.VideosVO;
import com.wq.utils.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VideosMapperCustom extends MyMapper<Videos> {
    public List<VideosVO> queryAllVideosVO(@Param("videoDesc") String videoDesc);
    public void addVideoLike(String videoId);
    public void reduceVideoLike(String videoId);
}