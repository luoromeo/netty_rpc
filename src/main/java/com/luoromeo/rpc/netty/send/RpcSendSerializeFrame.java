package com.luoromeo.rpc.netty.send;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.MutableClassToInstanceMap;
import com.luoromeo.rpc.netty.handle.send.HessianSendHandler;
import com.luoromeo.rpc.netty.handle.send.JdkNativeSendHandler;
import com.luoromeo.rpc.netty.handle.send.KryoSendHandler;
import com.luoromeo.rpc.netty.handle.send.NettyRpcSendHandler;
import com.luoromeo.rpc.netty.handle.send.ProtostuffSendHandler;
import com.luoromeo.rpc.serialize.support.RpcSerializeFrame;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

import io.netty.channel.ChannelPipeline;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月02日 10:54
 * @modified By
 */
public class RpcSendSerializeFrame implements RpcSerializeFrame {
    private static ClassToInstanceMap<NettyRpcSendHandler> handler = MutableClassToInstanceMap.create();

    static {
        handler.putInstance(JdkNativeSendHandler.class, new JdkNativeSendHandler());
        handler.putInstance(KryoSendHandler.class, new KryoSendHandler());
        handler.putInstance(HessianSendHandler.class, new HessianSendHandler());
        handler.putInstance(ProtostuffSendHandler.class, new ProtostuffSendHandler());
    }

    @Override
    public void select(RpcSerializeProtocol protocol, ChannelPipeline pipeline) {
        switch (protocol) {
            case JDKSERIALIZE: {
                handler.getInstance(JdkNativeSendHandler.class).handle(pipeline);
                break;
            }
            case KRYOSERIALIZE: {
                handler.getInstance(KryoSendHandler.class).handle(pipeline);
                break;
            }
            case HESSIANSERIALIZE: {
                handler.getInstance(HessianSendHandler.class).handle(pipeline);
                break;
            }
            case PROTOSTUFFSERIALIZE: {
                handler.getInstance(ProtostuffSendHandler.class).handle(pipeline);
                break;
            }
            default: {
                break;
            }
        }
    }
}
