package com.luoromeo.rpc.netty.server;

import java.util.Map;

import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 22:57
 * @modified By
 */
public class MessageRecvInitializeTaskAdapter extends AbstractMessageRecvInitializeTask {
    public MessageRecvInitializeTaskAdapter(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap) {
        super(request, response, handlerMap);
    }

    @Override
    protected void injectInvoke() {
        System.out.println("injectInvoke");
    }

    @Override
    protected void injectSuccInvoke(long invokeTimespan) {
        System.out.println("injectSuccInvoke");
        response.setInvokeTimespan(invokeTimespan);
    }

    @Override
    protected void injectFailInvoke(Throwable error) {
        System.out.println("injectFailInvoke");
    }

    @Override
    protected void injectFilterInvoke() {
        System.out.println("injectFilterInvoke");
    }

    @Override
    protected void acquire() {
        System.out.println("acquire");
    }

    @Override
    protected void release() {
        System.out.println("release");
    }
}
