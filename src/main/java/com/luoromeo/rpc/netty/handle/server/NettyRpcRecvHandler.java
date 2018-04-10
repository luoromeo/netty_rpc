package com.luoromeo.rpc.netty.handle.server;

import java.util.Map;

import io.netty.channel.ChannelPipeline;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 23:06
 * @modified By
 */
public interface NettyRpcRecvHandler {
    void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline);
}
