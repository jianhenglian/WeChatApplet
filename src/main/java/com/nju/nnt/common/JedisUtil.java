package com.nju.nnt.common;


import redis.clients.jedis.Jedis;

public class JedisUtil {
    private static Jedis jedis = null;
    public static Jedis getJedisClient(){
        if(jedis == null){
            jedis = new Jedis("139.196.157.116",6379);
        }
        return jedis;
    }
}
