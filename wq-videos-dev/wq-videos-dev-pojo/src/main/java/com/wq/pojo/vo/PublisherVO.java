package com.wq.pojo.vo;

/**
 * Created by qwu on 2018/8/6.
 */
public class PublisherVO {
    private UsersVO usersVO;
    private boolean userLikeVideo;

    public UsersVO getUsersVO() {
        return usersVO;
    }

    public void setUsersVO(UsersVO usersVO) {
        this.usersVO = usersVO;
    }

    public boolean isUserLikeVideo() {
        return userLikeVideo;
    }

    public void setUserLikeVideo(boolean userLikeVideo) {
        this.userLikeVideo = userLikeVideo;
    }
}
