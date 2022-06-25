package com.nju.nnt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nju.nnt.common.*;
import com.nju.nnt.entity.Message;
import com.nju.nnt.service.MessageService;
import com.nju.nnt.websocket.session.OnlineUserSession;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    MessageService messageService;


    @RequestMapping("/send")
    public Response send(@RequestBody JSONObject data){
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "from","from_avatar","from_name", "to","to_avatar","to_name","msg","token","k"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }
        try {
            JWTUtil.getInfoFromToken(data.getString("token"));
        }catch (Exception e){
            log.error("token无效");
            return Response.error("token无效");
        }

        String fromId = data.getString("from");
        String fromName = data.getString("from_name");
        String fromAvatar = data.getString("from_avatar");
        String toId = data.getString("to");
        String toName = data.getString("to_name");
        String toAvatar = data.getString("to_avatar");
        String k = data.getString("k");
        String msg = data.getString("msg");
        Message message = new Message();
        message.setFromName(fromName);
        message.setFromAvatar(fromAvatar);
        message.setFromId(fromId);
        message.setToId(toId);
        message.setToAvatar(toAvatar);
        message.setToName(toName);
        message.setMsg(msg);
        message.setK(k);
        message.setMsgId(new SnowflakeIdWorker(0,0).nextId());
//        if(OnlineUserSession.getChannel(toId) == null){
//            log.info("接受者不在线,将消息暂时缓存到redis中");
//            JedisUtil.getJedisClient().lpush("nnt-"+toId,JSON.toJSONString(message));
//
//        }else{
//            log.info("接受者在线,进行消息推送");
//            Channel toChannel = OnlineUserSession.getChannel(toId);
//            toChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
//        }
        messageService.insertMessage(message);
        return Response.success("发送消息成功");
    }

    @RequestMapping("/getDownLineMsg")
    public Response getDownLineMsg(@RequestBody JSONObject data){
        log.info("check params");
        String lossParams = CheckParams.check(data,new String[]{
                "user_id","token"
        });
        if (!"".equals(lossParams)){
            log.error("参数类型不匹配,缺少参数："+lossParams);
            return Response.error("参数类型不匹配,缺少参数："+lossParams);
        }
        try {
            JWTUtil.getInfoFromToken(data.getString("token"));
        }catch (Exception e){
            log.error("token无效");
            return Response.error("token无效");
        }
        String userId = data.getString("user_id");
        if(!JedisUtil.getJedisClient().exists("nnt-"+userId)){
            return Response.success(new ArrayList<>());
        }
        List<String> msgs = JedisUtil.getJedisClient().lrange("nnt-" + userId, 0, -1);
        JedisUtil.getJedisClient().del("nnt-" + userId);
        return Response.success(msgs);
    }
}
