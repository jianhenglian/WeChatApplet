package com.nju.nnt.controller;

import com.alibaba.fastjson.JSONObject;
import com.nju.nnt.common.CheckParams;
import com.nju.nnt.common.JWTUtil;
import com.nju.nnt.common.Response;
import com.nju.nnt.common.WeiXinUtil;
import com.nju.nnt.entity.Goods;
import com.nju.nnt.entity.User;
import com.nju.nnt.service.GoodsService;
import com.nju.nnt.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    GoodsService goodsService;


    @RequestMapping("/wxlogin")
    public Response WxLogin(@RequestBody JSONObject data){
        log.info("检查参数");
        String lossParams = CheckParams.check(data,new String[]{
                "code","rawData","signature"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }
        String rawData = data.getString("rawData");
        String code = data.getString("code");
        String signature = data.getString("signature");
        JSONObject userInfo = data.getJSONObject("rawData");

        log.info("wxlogin返回的code为: {}",code);
        log.info("接收到的用户信息为: {}",userInfo);
        log.info("接收到的数据签名为: {}",signature);

        //利用code得到openid和sessionKey
        JSONObject sessionKeyOrOpenId = WeiXinUtil.getSessionKeyOrOpenId(code);

        log.info("利用code调用接口得到: {}",sessionKeyOrOpenId);
        String openId = sessionKeyOrOpenId.getString("openid");
        String sessionKey = sessionKeyOrOpenId.getString("session_key");
        //利用sessionKey鉴定数据的真实性
        String mySignature = DigestUtils.sha1Hex(userInfo + sessionKey);

        log.info("利用sessionKey算出的真实签名为: {}",mySignature);
        if(!mySignature.equals(signature)){
            //数据真实性异常
            //log.error("数据签名校验失败");
            //return Response.error(400,"数据签名校验失败",null);
        }
        //利用openId实现注册或者登录
        User user = userService.selectUserByOpenId(openId);
        if(user==null){
            //需要注册
            User newUser = new User();
            newUser.setOpenId(openId);
            newUser.setGender(userInfo.getString("gender"));
            newUser.setAvatarUrl(userInfo.getString("avatarUrl"));
            newUser.setNickname(userInfo.getString("nickname"));
            userService.registerUser(newUser);
            user = newUser;
        }
        String token = null;
        //TODO 注册或者登录之后生成token返回给客户端
        try {
            token = JWTUtil.generateToken(user, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("生成token:{}",token);

        JSONObject respData = new JSONObject();
        respData.put("token",token);
        respData.put("userId",openId );
        log.info("openId = {}",openId);
        return Response.success(respData,code);
    }

    @RequestMapping("/detail")
    public Response getUserDetail(@RequestParam Long userId){
        User userDetail = userService.getUserDetail(userId);
        log.info("UserDetail: {}",userDetail);
        return Response.success(userDetail);
    }

    /**
     * 这里还有许多逻辑需要处理
     * 首先，进入页面后，如果商品已经被收藏，要变成红色，同时点击后取消收藏
     * 其次，个人收藏页面的渲染逻辑也要处理
     * @param data
     * @return
     */
    @PostMapping("/collectGoods")
    public Response collectGoods(@RequestBody JSONObject data)
    {
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "userId","goodsId"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }

        log.info("接收到的数据为：{}",data);

        String userId = data.getString("userId");
        Long goodsId = Long.parseLong(data.getString("goodsId"));
        log.info("userId: {}", userId);
        log.info("goodsId: {}", goodsId);
        if(userService.hasCollectGoods(goodsId, userId))
        {
            userService.cancelCollectGoods(goodsId, userId);
            return Response.success("取消收藏成功");
        }
        else
        {
            userService.collectGoods(goodsId, userId);
            return Response.success("收藏成功");
        }
    }

    @RequestMapping("/userGoodsRelation")
    public Response getUserGoodsRelation(@RequestParam String userId, @RequestParam String goodsId)
    {
        log.info("userId: {}", userId);
        log.info("goodsId: {}", goodsId);
        boolean result = userService.hasCollectGoods(Long.parseLong(goodsId),userId);
        return Response.success(result);
    }

    @RequestMapping("/listAllCollections")
    public Response getAllUserCollections(@RequestParam String userId, @RequestParam int page)
    {
        log.info("userId: {}", userId);
        List<Goods> result =goodsService.listAllCollectGoods(userService.getAllCollectInfo(userId), page, 10);
        log.info(result.toString());
        return Response.success(result);
    }


}
