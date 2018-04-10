package com.luoromeo.rpc.netty.handle.server;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Map;

import com.luoromeo.rpc.netty.server.MessageRecvHandler;
import com.luoromeo.rpc.serialize.support.MessageCodecUtil;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 23:08
 * @modified By
 */
public class JdkNativeRecvHandler implements NettyRpcRecvHandler {
    @Override
    public void handle(Map<String, Object> handlerMap, ChannelPipeline pipeline) {
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageCodecUtil.MESSAGE_LENGTH, 0, MessageCodecUtil.MESSAGE_LENGTH));
        pipeline.addLast(new LengthFieldPrepender(MessageCodecUtil.MESSAGE_LENGTH));
        pipeline.addLast(new ObjectEncoder());
        pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
        pipeline.addLast(new MessageRecvHandler(handlerMap));
    }
}
