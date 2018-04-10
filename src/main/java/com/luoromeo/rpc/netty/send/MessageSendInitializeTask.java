package com.luoromeo.rpc.netty.send;

import java.net.InetSocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.luoromeo.rpc.core.RpcSystemConfig;
import com.luoromeo.rpc.netty.RpcServerLoader;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
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

    MessageSendInitializeTask(EventLoopGroup eventLoopGroup, InetSocketAddress serverAddress, RpcSerializeProtocol protocol) {
        this.eventLoopGroup = eventLoopGroup;
        this.serverAddress = serverAddress;
        this.protocol = protocol;
    }

    @Override
    public Boolean call() {
        Bootstrap b = new Bootstrap();
        b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).remoteAddress(serverAddress);
        b.handler(new MessageSendChannelInitializer().buildRpcSerializeProtocol(protocol));

        ChannelFuture channelFuture = b.connect();
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if (channelFuture1.isSuccess()) {
                MessageSendHandler handler = channelFuture1.channel().pipeline().get(MessageSendHandler.class);
                RpcServerLoader.getInstance().setMessageSendHandler(handler);
            } else {
                EventLoop loop = (EventLoop) eventLoopGroup.schedule(() -> {
                    System.out.println("NettyRPC server is down,start to reconnecting to: " + serverAddress.getAddress().getHostAddress()
                            + ':' + serverAddress.getPort());
                    call();
                }, RpcSystemConfig.SYSTEM_PROPERTY_CLIENT_RECONNECT_DELAY, TimeUnit.SECONDS);
            }
        });
        return Boolean.TRUE;
    }
}
