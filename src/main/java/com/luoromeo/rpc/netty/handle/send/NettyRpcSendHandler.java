package com.luoromeo.rpc.netty.handle.send;

import io.netty.channel.ChannelPipeline;

public interface NettyRpcSendHandler {
    void handle(ChannelPipeline pipeline);
}
