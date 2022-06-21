package com.nju.nnt.websocket;

import com.nju.nnt.websocket.handler.ChatHandler;
import com.nju.nnt.websocket.handler.WSConnectHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WSServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        //websocket依赖http  故需要http编解码器
        pipeline.addLast(new HttpServerCodec());
        //对大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        //对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
        pipeline.addLast(new HttpObjectAggregator(1024*64));

        //==============以上是对http的支持===============//
        pipeline.addLast(new WSConnectHandler());
        //指定客户端访问路由  帮忙处理握手等繁重工作
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //添加自定义handler
        pipeline.addLast(new ChatHandler());


    }
}
