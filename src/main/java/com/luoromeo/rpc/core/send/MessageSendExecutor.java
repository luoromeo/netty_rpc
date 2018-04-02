package com.luoromeo.rpc.core.send;

import com.google.common.reflect.Reflection;
import com.luoromeo.rpc.core.RpcServerLoader;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

/**
 * @description 客户端执行模块
 * @author zhanghua.luo
 * @date 2018年03月29日 11:44
 * @modified By
 */
public class MessageSendExecutor {
    private RpcServerLoader loader = RpcServerLoader.getInstance();

    public MessageSendExecutor() {
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

    public static <T> T execute(Class<T> rpcInterface) {
        return (T) Reflection.newProxy(rpcInterface, new MessageSendProxy<T>());
    }
}
