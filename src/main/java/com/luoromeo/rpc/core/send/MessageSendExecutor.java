package com.luoromeo.rpc.core.send;

import java.lang.reflect.Proxy;

import com.luoromeo.rpc.core.RpcServerLoader;

/**
 * @description 客户端执行模块
 * @author zhanghua.luo
 * @date 2018年03月29日 11:44
 * @modified By
 */
public class MessageSendExecutor {
    private RpcServerLoader loader = RpcServerLoader.getInstance();

    public MessageSendExecutor(String serverAddress) {
        loader.load(serverAddress);
    }

    public void stop() {
        loader.unLoad();
    }

    @SuppressWarnings("unchecked")
    public static <T> T execute(Class<T> rpcInterface) {
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader(), new Class[] { rpcInterface }, new MessageSendProxy<T>(rpcInterface));
    }
}
