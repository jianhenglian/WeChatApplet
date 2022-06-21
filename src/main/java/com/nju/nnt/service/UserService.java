package com.nju.nnt.service;

import com.nju.nnt.entity.Goods;
import com.nju.nnt.entity.User;
import com.nju.nnt.entity.UserCollect;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Int;

import java.util.List;

@Service
public interface UserService {
    User selectUserByOpenId(String openId);

    void registerUser(User user);

    User getUserDetail(Long userId);

    void collectGoods(Long goodsId, String userId);

    void cancelCollectGoods(Long goodsId, String userId);

    Boolean hasCollectGoods(Long goodsId, String userId);

    List<UserCollect> getAllCollectInfo(String userId);
}
