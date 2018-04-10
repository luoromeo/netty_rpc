package com.luoromeo.rpc.netty.handle.server;

import java.util.Map;

import com.luoromeo.rpc.netty.server.MessageRecvHandler;
import com.luoromeo.rpc.serialize.support.protostuff.ProtostuffCodecUtil;
import com.luoromeo.rpc.serialize.support.protostuff.ProtostuffDecoder;
import com.luoromeo.rpc.serialize.support.protostuff.ProtostuffEncoder;

import io.netty.channel.ChannelPipeline;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 23:10
 * @modified By
 */
public class ProtostuffRecvHandler implements NettyRpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        ProtostuffCodecUtil util = new ProtostuffCodecUtil();
        util.setRpcDirect(true);
        pipeline.addLast(new ProtostuffEncoder(util));
        pipeline.addLast(new ProtostuffDecoder(util));
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }
}
