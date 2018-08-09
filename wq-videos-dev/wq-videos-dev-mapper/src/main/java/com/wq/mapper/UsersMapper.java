package com.wq.mapper;

import com.wq.pojo.Users;
import com.wq.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
    //用户收获喜欢增1
    public void addReceiveLikeCount(String userId);
    //用户收获喜欢数减1
    public void reduceReceiveLikeCount(String userId);
    //用户关注数增加
    public void addAttentionCount(String userId);
    //用户关注数减少
    public void reduceAttentionCount(String userId);
    //用户粉丝数增加
    public void addFansCount(String userId);
    //用户粉丝数减少
    public void reduceFansCount(String userId);
}