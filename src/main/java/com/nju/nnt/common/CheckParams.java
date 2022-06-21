package com.nju.nnt.common;

import com.alibaba.fastjson.JSONObject;

public class CheckParams {
    public static String check(JSONObject data, String[] params){
        String lossParams = "";
        for (String param : params){
            if (!data.containsKey(param)){
                lossParams = lossParams + " " + param;
            }
        }
        return lossParams;
    }
}
