package com.nju.nnt.websocket.session;


import io.netty.channel.Channel;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Data
public class OnlineUserSession {

    private static volatile  Map<String, Channel> idChannelMap = new ConcurrentHashMap<>();
    private static volatile  Map<Channel, String> channelIdMap = new ConcurrentHashMap<>();
    private static volatile  Map<Channel,Map<String,Object>> channelAttributesMap = new ConcurrentHashMap<>();


    public static void bind(Channel channel, String userId) {
        idChannelMap.put(userId, channel);
        channelIdMap.put(channel, userId);
        channelAttributesMap.put(channel, new ConcurrentHashMap<>());
    }

    public static void unbind(Channel channel) {
        String username = channelIdMap.remove(channel);
        idChannelMap.remove(username);
        channelAttributesMap.remove(channel);
    }


    public static Object getAttribute(Channel channel, String key) {
        return channelAttributesMap.get(channel).get(key);
    }


    public static void setAttribute(Channel channel, String key, Object value) {
        channelAttributesMap.get(channel).put(key, value);
    }


    public static Channel getChannel(String userId) {
        return idChannelMap.get(userId);
    }

    public static String getUserId(Channel channel){
        return channelIdMap.get(channel);
    }


}

