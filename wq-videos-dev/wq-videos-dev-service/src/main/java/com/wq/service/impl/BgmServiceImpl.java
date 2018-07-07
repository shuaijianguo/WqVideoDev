package com.wq.service.impl;

import com.wq.mapper.BgmMapper;
import com.wq.mapper.UsersMapper;
import com.wq.pojo.Bgm;
import com.wq.pojo.Users;
import com.wq.service.BgmService;
import com.wq.service.UserService;
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
public class BgmServiceImpl implements BgmService {

    @Autowired
    private BgmMapper bgmMapper;
    @Autowired
    private Sid sid;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<Bgm> queryBgmList() {
        List<Bgm> bgms = bgmMapper.selectAll();
        return bgms;
    }
}
