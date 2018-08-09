package com.wq.service.impl;

import com.wq.mapper.UsersFansMapper;
import com.wq.mapper.UsersLikeVideosMapper;
import com.wq.mapper.UsersMapper;
import com.wq.pojo.Users;
import com.wq.pojo.UsersFans;
import com.wq.pojo.UsersLikeVideos;
import com.wq.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

import java.util.List;

/**
 * Created by wuqingvika on 2018/7/1.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private UsersLikeVideosMapper usersLikeVideosMapper;
    @Autowired
    private UsersFansMapper usersFansMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUsernameIsExist(String username) {
        Users users = new Users();
        users.setUsername(username);
        Users result = usersMapper.selectOne(users);
        return result == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveUser(Users user) {
        String userId = sid.nextShort();
        user.setId(userId);
        usersMapper.insert(user);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserByNameAndPwd(String username, String pwd) {
        Example userExample=new Example(Users.class);
        Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("username",username);
        criteria.andEqualTo("password",pwd);
        Users users = usersMapper.selectOneByExample(userExample);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserInfo(Users users) {
        Example userExample=new Example(Users.class);
        Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("id",users.getId());
        usersMapper.updateByExampleSelective(users,userExample);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserByUserId(String userId) {
        Example userExample=new Example(Users.class);
        Criteria criteria=userExample.createCriteria();
        criteria.andEqualTo("id",userId);
        Users users = usersMapper.selectOneByExample(userExample);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean isUserLikeVideo(String userId,String videoId) {
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(videoId)){
            return false;//
        }
        Example example=new Example(UsersLikeVideos.class);//这里踩了坑
        Criteria criteria=example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("videoId",videoId);
        List<UsersLikeVideos> usersLikeVideos = usersLikeVideosMapper.selectByExample(example);
        if(usersLikeVideos!=null&&!usersLikeVideos.isEmpty()){
            return true;
        }
        return false;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserFans(String userId, String fansId) {
        //保存用户粉丝关系到关系表
        UsersFans uf=new UsersFans();
        uf.setId(sid.nextShort());
        uf.setUserId(userId);
        uf.setFanId(fansId);
        usersFansMapper.insert(uf);
        //增加粉丝的关注数
        usersMapper.addAttentionCount(fansId);
        //增加用户的粉丝数
        usersMapper.addFansCount(userId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUserFans(String userId, String fansId) {
        //删除用户粉丝关系到关系表
       Example example=new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fansId);
        usersFansMapper.deleteByExample(example);
        //减少粉丝的关注数
        usersMapper.reduceAttentionCount(fansId);
        //减少用户的粉丝数
        usersMapper.reduceFansCount(userId);
    }

    @Override
    public boolean isFollowPublisher(String userId, String fanId) {
        Example example=new Example(UsersFans.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId",userId);
        criteria.andEqualTo("fanId",fanId);

        List<UsersFans> usersFans = usersFansMapper.selectByExample(example);
        if(usersFans!=null&&!usersFans.isEmpty()){
            return true;
        }
        return false;
    }
}
