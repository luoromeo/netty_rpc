package com.luoromeo.rpc.netty.handle.server;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

import com.luoromeo.rpc.netty.server.MessageRecvHandler;
import com.luoromeo.rpc.serialize.support.kryo.KryoCodecUtil;
import com.luoromeo.rpc.serialize.support.kryo.KryoDecoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoEncoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoPoolFactory;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 23:09
 * @modified By
 */
public class KryoRecvHandler implements NettyRpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        KryoCodecUtil util = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
        pipeline.addLast(new KryoEncoder(util));
        pipeline.addLast(new KryoDecoder(util));
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }
}
