package com.luoromeo.rpc.netty.handle.send;

import com.luoromeo.rpc.netty.send.MessageSendHandler;
import com.luoromeo.rpc.serialize.support.hessian.HessianCodecUtil;
import com.luoromeo.rpc.serialize.support.hessian.HessianDecoder;
import com.luoromeo.rpc.serialize.support.hessian.HessianEncoder;

import io.netty.channel.ChannelPipeline;

public class HessianSendHandler implements NettyRpcSendHandler {
    @Override
    public void handle(ChannelPipeline pipeline) {
        HessianCodecUtil util = new HessianCodecUtil();
        pipeline.addLast(new HessianEncoder(util));
        pipeline.addLast(new HessianDecoder(util));
        pipeline.addLast(new MessageSendHandler());
    }
}
