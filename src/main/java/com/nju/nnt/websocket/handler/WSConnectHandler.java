package com.nju.nnt.websocket.handler;

import com.nju.nnt.common.JWTUtil;
import com.nju.nnt.entity.User;
import com.nju.nnt.websocket.session.OnlineUserSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WSConnectHandler extends SimpleChannelInboundHandler<Object> {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null != msg && msg instanceof FullHttpRequest) {
            log.info("准备提取token");
            //转化为http请求
            FullHttpRequest request = (FullHttpRequest) msg;
            //拿到请求地址
            String uri = request.uri();
            log.info("uri:{}",uri);
            //判断是不是websocket请求，如果是拿出我们传递的参数（我的是token）
            String origin = request.headers().get("Origin");
            if (null == origin) {
                log.info("不是webSocker请求，关闭连接");
                ctx.close();
                return;
            } else {
                if (null != uri && uri.contains("/ws") && uri.contains("?")) {
                    String[] uriArray = uri.split("\\?");
                    if (null != uriArray && uriArray.length > 1) {
                        String[] paramsArray = uriArray[1].split("=");
                        if (null != paramsArray && paramsArray.length > 1) {
                            String token = paramsArray[1];
                            log.info("提取token成功,token值为:{}",token);
                            //校验token
                            User infoFromToken;
                            try {
                               infoFromToken = JWTUtil.getInfoFromToken(token);
                            }catch (Exception e){
                                log.error("token无效");
                                ctx.close();
                                return;
                            }
                            log.info("根据token得到的用户信息:{}",infoFromToken);
                            //将用户保存到Session
                            OnlineUserSession.bind(ctx.channel(),infoFromToken.getOpenId());
                        }
                    }
                    //重新设置请求地址
                    request.setUri("/ws");
                }
            }
        }
        //接着建立请求
        ctx.fireChannelRead(msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

    }


}
