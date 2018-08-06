package com.wq.mapper;

import com.wq.pojo.Users;
import com.wq.utils.MyMapper;

public interface UsersMapper extends MyMapper<Users> {
    public void addReceiveLikeCount(String userId);
    public void reduceReceiveLikeCount(String userId);
}