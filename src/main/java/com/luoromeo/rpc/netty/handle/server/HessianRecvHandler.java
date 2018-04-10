package com.luoromeo.rpc.netty.handle.server;

import io.netty.channel.ChannelPipeline;

import java.util.Map;

import com.luoromeo.rpc.netty.server.MessageRecvHandler;
import com.luoromeo.rpc.serialize.support.hessian.HessianCodecUtil;
import com.luoromeo.rpc.serialize.support.hessian.HessianDecoder;
import com.luoromeo.rpc.serialize.support.hessian.HessianEncoder;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 23:08
 * @modified By
 */
public class HessianRecvHandler implements NettyRpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        HessianCodecUtil util = new HessianCodecUtil();
        pipeline.addLast(new HessianEncoder(util));
        pipeline.addLast(new HessianDecoder(util));
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }
}
