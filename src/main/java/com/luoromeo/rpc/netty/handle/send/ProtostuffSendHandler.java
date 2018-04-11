package com.luoromeo.rpc.netty.handle.send;

import com.luoromeo.rpc.netty.send.MessageSendHandler;
import com.luoromeo.rpc.serialize.support.protostuff.ProtostuffCodecUtil;
import com.luoromeo.rpc.serialize.support.protostuff.ProtostuffDecoder;
import com.luoromeo.rpc.serialize.support.protostuff.ProtostuffEncoder;

import io.netty.channel.ChannelPipeline;

public class ProtostuffSendHandler implements NettyRpcSendHandler {
    @Override
    public void handle(ChannelPipeline pipeline) {
        ProtostuffCodecUtil util = new ProtostuffCodecUtil();
        util.setRpcDirect(false);
        pipeline.addLast(new ProtostuffEncoder(util));
        pipeline.addLast(new ProtostuffDecoder(util));
        pipeline.addLast(new MessageSendHandler());
    }
}
