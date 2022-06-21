package com.nju.nnt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.nju.nnt.entity.Goods;
import com.nju.nnt.entity.User;
import com.nju.nnt.entity.UserCollect;
import com.nju.nnt.mapper.UserCollectMapper;
import com.nju.nnt.mapper.UserMapper;
import com.nju.nnt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    UserCollectMapper userCollectMapper;
    @Override
    public User selectUserByOpenId(String openId) {
        User user = userMapper.selectById(openId);
        return user;
    }

    @Override
    public void registerUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public User getUserDetail(Long userId) {
        User user = userMapper.selectById(userId);
        return user;
    }

    @Override
    public void collectGoods(Long goodsId, String openId)
    {
        UserCollect userCollect = new UserCollect(openId, goodsId);
        userCollectMapper.insert(userCollect);
    }

    @Override
    public void cancelCollectGoods(Long goodsId, String userId)
    {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        queryWrapper.eq("open_id", userId);
        userCollectMapper.delete(queryWrapper);
    }

    @Override
    public Boolean hasCollectGoods(Long goodsId, String userId)
    {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("goods_id", goodsId);
        queryWrapper.eq("open_id", userId);
        return userCollectMapper.selectOne(queryWrapper) != null;
    }

    @Override
    public List<UserCollect> getAllCollectInfo(String userId)
    {
        QueryWrapper<UserCollect> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("open_id", userId);
        return userCollectMapper.selectList(queryWrapper);
    }

}
