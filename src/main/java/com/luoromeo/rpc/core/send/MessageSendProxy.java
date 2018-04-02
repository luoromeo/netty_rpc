package com.luoromeo.rpc.core.send;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

import com.luoromeo.rpc.core.MessageCallBack;
import com.luoromeo.rpc.core.RpcServerLoader;
import com.luoromeo.rpc.model.MessageRequest;

/**
 * @description Rpc客户端消息处理
 * @author zhanghua.luo
 * @date 2018年03月29日 11:51
 * @modified By
 */
public class MessageSendProxy<T> implements InvocationHandler {

    private Class<T> cls;

    public MessageSendProxy(Class<T> cls) {
        this.cls = cls;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request = new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setTypeParameters(method.getParameterTypes());
        request.setParametersVal(args);

        MessageSendHandler handler = RpcServerLoader.getInstance().getMessageSendHandler();
        MessageCallBack callBack = handler.senRequest(request);
        return callBack.start();
    }
}