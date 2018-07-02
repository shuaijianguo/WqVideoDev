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

    /**
     * 根据用户名密码查询该用户
     * @param username
     * @param pwd
     * @return
     */
    public Users queryUserByNameAndPwd(String username,String pwd);
}
