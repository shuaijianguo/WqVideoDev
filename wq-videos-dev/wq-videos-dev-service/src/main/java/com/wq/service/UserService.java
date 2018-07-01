package com.wq.service;

import com.wq.pojo.Users;

/**
 * Created by wuqingvika on 2018/7/1.
 */
public interface UserService {
    /**
     * 判断用户名是否存在
     * @param username
     * @return
     */
    public boolean queryUsernameIsExist(String username);

    /**
     * 注册新用户
     * @param user
     */
    public void saveUser(Users user);
}
