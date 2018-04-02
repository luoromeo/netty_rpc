package com.luoromeo.rpc.core.send;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

import com.luoromeo.rpc.core.RpcServerLoader;

/**
 * @description Rpc客户端线程任务处理
 * @author zhanghua.luo
 * @date 2018年03月29日 10:53
 * @modified By
 */
public class MessageSendInitializeTask implements Runnable {

    private EventLoopGroup eventLoopGroup = null;

    private InetSocketAddress serverAddress = null;

    private RpcServerLoader loader = null;

    public MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcServerLoader loader) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress = serverAddress;
        this.loader = loader;
    }

    @Override
    public void run() {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new MessageSendChannelInitializer());

        ChannelFuture channelFuture = b.connect(serverAddress);
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                MessageSendHandler handler = channelFuture1.channel().pipeline().get(MessageSendHandler.class);
                MessageSendInitializeTask.this.loader.setMessageSendHandler(handler);
            }
        });
    }
}
