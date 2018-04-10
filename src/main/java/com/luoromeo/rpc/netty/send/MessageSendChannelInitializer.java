package com.luoromeo.rpc.netty.send;

import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @description Rpc端管道初始化
 * @author zhanghua.luo
 * @date 2018年03月29日 11:00
 * @modified By
 */
public class MessageSendChannelInitializer extends ChannelInitializer<SocketChannel> {

    private RpcSerializeProtocol protocol;
    private RpcSendSerializeFrame frame = new RpcSendSerializeFrame();

    MessageSendChannelInitializer buildRpcSerializeProtocol(RpcSerializeProtocol protocol) {
        this.protocol = protocol;
        return this;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        frame.select(protocol, pipeline);
    }
}
