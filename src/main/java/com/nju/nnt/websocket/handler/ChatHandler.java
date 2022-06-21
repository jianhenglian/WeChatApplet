package com.nju.nnt.websocket.handler;

import com.nju.nnt.websocket.session.OnlineUserSession;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    //用于管理所有客户端的channel
    private static ChannelGroup clients =
            new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String content = msg.text();
        log.info("userId:{}  向服务器发来消息，消息内容:{}", OnlineUserSession.getUserId(ctx.channel()),content);
        for (Channel client : clients) {
            client.writeAndFlush(new TextWebSocketFrame("服务器在"+ LocalDateTime.now()+"收到消息，消息为: "+content));
        }
    }

    /**
     * 当客户端连接到服务端后触发
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("用户id:{} 上线",OnlineUserSession.getUserId(ctx.channel()));
        clients.add(ctx.channel());
    }


    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //clients.remove(ctx.channel());
        log.info("用户id:{}  下线",OnlineUserSession.getUserId(ctx.channel()));

        OnlineUserSession.unbind(ctx.channel());
    }
}
