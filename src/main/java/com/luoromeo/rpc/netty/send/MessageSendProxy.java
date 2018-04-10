package com.luoromeo.rpc.netty.send;

import java.lang.reflect.Method;
import java.util.UUID;

import com.google.common.reflect.AbstractInvocationHandler;
import com.luoromeo.rpc.core.MessageCallBack;
import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.netty.RpcServerLoader;

/**
 * @description Rpc客户端消息处理
 * @author zhanghua.luo
 * @date 2018年03月29日 11:51
 * @modified By
 */
public class MessageSendProxy<T> extends AbstractInvocationHandler {

    @Override
    public Object handleInvocation(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParametersVal(args);

        MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
        MessageCallBack callBack = handler.sendRequest(request);
        return callBack.start();
    }
}
