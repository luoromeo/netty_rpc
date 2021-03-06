package com.luoromeo.rpc.netty.resolver;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 14:44
 * @modified By
 */
public class ApiEchoInitializer extends ChannelInitializer<SocketChannel> {

    private final SslContext sslCtx;

    public ApiEchoInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null) {
            p.addLast(sslCtx.newHandler(ch.alloc()));
        }
        p.addLast(new HttpServerCodec());
        p.addLast(new ApiEchoHandler());
    }
}
