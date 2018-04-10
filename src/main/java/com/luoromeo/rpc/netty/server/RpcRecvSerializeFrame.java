package com.luoromeo.rpc.netty.server;

import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.luoromeo.rpc.netty.handle.server.HessianRecvHandler;
import com.luoromeo.rpc.netty.handle.server.JdkNativeRecvHandler;
import com.luoromeo.rpc.netty.handle.server.KryoRecvHandler;
import com.luoromeo.rpc.netty.handle.server.NettyRpcRecvHandler;
import com.luoromeo.rpc.netty.handle.server.ProtostuffRecvHandler;
import com.luoromeo.rpc.serialize.support.RpcSerializeFrame;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

import io.netty.channel.ChannelPipeline;

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

    private static ClassToInstanceMap<NettyRpcRecvHandler> handler = MutableClassToInstanceMap.create();

    static {
        handler.putInstance(JdkNativeRecvHandler.class, new JdkNativeRecvHandler());
        handler.putInstance(KryoRecvHandler.class, new KryoRecvHandler());
        handler.putInstance(HessianRecvHandler.class, new HessianRecvHandler());
        handler.putInstance(ProtostuffRecvHandler.class, new ProtostuffRecvHandler());
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE: {
                handler.getInstance(JdkNativeRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            case KRYOSERIALIZE: {
                handler.getInstance(KryoRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            case HESSIANSERIALIZE: {
                handler.getInstance(HessianRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            case PROTOSTUFFSERIALIZE: {
                handler.getInstance(ProtostuffRecvHandler.class).handle(handlerMap, pipeline);
                break;
            }
            default: {
                break;
            }
        }
    }
}
