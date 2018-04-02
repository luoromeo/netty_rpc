package com.luoromeo.rpc.core.recv;

import java.util.Map;

import com.luoromeo.rpc.serialize.support.RpcSerializeFrame;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;
import com.luoromeo.rpc.serialize.support.hessian.HessianCodecUtil;
import com.luoromeo.rpc.serialize.support.hessian.HessianDecoder;
import com.luoromeo.rpc.serialize.support.hessian.HessianEncoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoCodecUtil;
import com.luoromeo.rpc.serialize.support.kryo.KryoDecoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoEncoder;
import com.luoromeo.rpc.serialize.support.kryo.KryoPoolFactory;

import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年03月30日 17:26
 * @modified By
 */
public class RpcRecvSerializeFrame implements RpcSerializeFrame {

    private Map<String, Object> handlerMap = null;

    public RpcRecvSerializeFrame(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    // 后续可优化成通过spring ioc方式注入
    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case KRYOSERIALIZE:
                KryoCodecUtil kryoCodecUtil = new KryoCodecUtil(KryoPoolFactory.getKryoPoolInstance());
                pipeline.addLast(new KryoEncoder(kryoCodecUtil));
                pipeline.addLast(new KryoDecoder(kryoCodecUtil));
                pipeline.addLast(new MessageRecvHandler(handlerMap));
                break;
            case HESSIANSERIALIZE:
                HessianCodecUtil hessianCodecUtil = new HessianCodecUtil();
                pipeline.addLast(new HessianEncoder(hessianCodecUtil));
                pipeline.addLast(new HessianDecoder(hessianCodecUtil));
                pipeline.addLast(new MessageRecvHandler(handlerMap));
                break;
            case JDKSERIALIZE:
            default:
                // ObjectDecoder的基类半包解码器LengthFieldBasedFrameDecoder的报文格式保持兼容。因为底层的父类LengthFieldBasedFrameDecoder
                // 的初始化参数即为super(maxObjectSize, 0, 4, 0, 4);
                pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageRecvChannelInitializer.MESSAGE_LENGTH, 0,
                        MessageRecvChannelInitializer.MESSAGE_LENGTH));
                // 利用LengthFieldPrepender回填补充ObjectDecoder消息报文头
                pipeline.addLast(new LengthFieldPrepender(MessageRecvChannelInitializer.MESSAGE_LENGTH));
                pipeline.addLast(new ObjectEncoder());
                // 考虑到并发性能，采用weakCachingConcurrentResolver缓存策略。一般情况使用:cacheDisabled即可
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                pipeline.addLast(new MessageRecvHandler(handlerMap));
                break;
        }
    }
}
