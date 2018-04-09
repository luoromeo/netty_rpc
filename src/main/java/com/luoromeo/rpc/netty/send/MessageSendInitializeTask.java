package com.luoromeo.rpc.netty.send;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;

import com.luoromeo.rpc.core.RpcServerLoader;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @description Rpc客户端线程任务处理
 * @author zhanghua.luo
 * @date 2018年03月29日 10:53
 * @modified By
 */
public class MessageSendInitializeTask implements Callable<Boolean> {

    private EventLoopGroup eventLoopGroup = null;

    private InetSocketAddress serverAddress = null;

    private RpcSerializeProtocol protocol;

    public MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcSerializeProtocol protocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress = serverAddress;
        this.protocol = protocol;
    }

    @Override
    public Boolean call() {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true);
        b.handler(new MessageSendChannelInitializer().buildRpcSerializeProtocol(protocol));

        ChannelFuture channelFuture = b.connect(serverAddress);
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                MessageSendHandler handler = channelFuture1.channel().pipeline().get(MessageSendHandler.class);
                RpcServerLoader.getInstance().setMessageSendHandler(handler);
            }
        });
        return Boolean.TRUE;
    }
}
