package com.luoromeo.rpc.netty.send;

import com.google.common.reflect.Reflection;
import com.luoromeo.rpc.netty.RpcServerLoader;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

/**
 * @description 客户端执行模块
 * @author zhanghua.luo
 * @date 2018年03月29日 11:44
 * @modified By
 */
public class MessageSendExecutor {
    private static class MessageSendExecutorHolder {
        private static final MessageSendExecutor INSTANCE = new MessageSendExecutor();
    }

    public static MessageSendExecutor getInstance() {
        return MessageSendExecutorHolder.INSTANCE;
    }

    private RpcServerLoader loader = RpcServerLoader.getInstance();

    private MessageSendExecutor() {

    }

    public MessageSendExecutor(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        loader.load(serverAddress, serializeProtocol);
    }

    public void setRpcServerLoader(String serverAddress, RpcSerializeProtocol serializeProtocol) {
        loader.load(serverAddress, serializeProtocol);
    }

    public void stop() {
        loader.unLoad();
    }

    public <T> T execute(Class<T> rpcInterface) {
        return Reflection.newProxy(rpcInterface, new MessageSendProxy<T>());
    }
}
