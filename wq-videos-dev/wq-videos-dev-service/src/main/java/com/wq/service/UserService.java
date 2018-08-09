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

    /**
     * 修改用户信息
     * @param users
     */
    public void updateUserInfo(Users users);

    /**
     * 根据用户Id查询该用户
     * @param userId
     * @return
     */
    public Users queryUserByUserId(String userId);

    /**
     * 判断用户是否喜欢该视频
     * @param userId
     * @param videoId
     * @return
     */
    public boolean isUserLikeVideo(String userId,String videoId);

    /**
     * 用户关注
     * @param userId
     * @param fansId
     */
    public void saveUserFans(String userId,String fansId);

    /**
     * 用户取消关注
     * @param userId
     * @param fansId
     */
    public void deleteUserFans(String userId,String fansId);

    /**
     * 判断用户是否关注 在加载个人信息页的时候
     * @param userId
     * @param fanId
     * @return
     */
    public boolean isFollowPublisher(String userId,String fanId);
}
