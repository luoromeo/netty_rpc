package com.luoromeo.rpc.netty.handle.send;

import com.luoromeo.rpc.netty.send.MessageSendHandler;
import com.luoromeo.rpc.serialize.support.kryo.KryoCodecUtil;
import com.luoromeo.rpc.serialize.support.kryo.KryoDecoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoEncoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoPoolFactory;

import io.netty.channel.ChannelPipeline;

public class KryoSendHandler implements NettyRpcSendHandler {
    @Override
    public void handle(ChannelPipeline pipeline) {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(util));
        pipeline.addLast(new KryoDecoder(util));
        pipeline.addLast(new MessageSendHandler());
    }
}
