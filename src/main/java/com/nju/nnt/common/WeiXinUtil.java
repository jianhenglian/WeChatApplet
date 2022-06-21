package com.nju.nnt.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

public class WeiXinUtil {


    @Value("${weixin.appid}")
    private static String appId;

    @Value("${weixin.appsecret}")
    private static String appSecret;

    public static JSONObject getSessionKeyOrOpenId(String code) {

        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session?appid=wxc5c699af7ce21427&secret=32e0827fdaa943f3405323f27114116e&js_code="+code+"&grant_type=authorization_code";
        String s = OkHttp.get(requestUrl);
        return JSONObject.parseObject(s);
    }


}
