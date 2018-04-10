package com.luoromeo.rpc.netty;

import java.util.Map;
import java.util.concurrent.Callable;

import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;
import com.luoromeo.rpc.netty.server.MessageRecvInitializeTask;
import com.luoromeo.rpc.netty.server.MessageRecvInitializeTaskAdapter;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 22:56
 * @modified By
 */
public class RecvInitializeTaskFacade {

    private MessageRequest request;
    private MessageResponse response;
    private Map<String, Object> handlerMap;

    public RecvInitializeTaskFacade(MessageRequest request, MessageResponse response, Map<String, Object> handlerMap) {
        this.request = request;
        this.response = response;
        this.handlerMap = handlerMap;
    }

    public Callable<Boolean> getTask() {
        return  new MessageRecvInitializeTaskAdapter(request, response, handlerMap);
    }

    private Callable<Boolean> getMetricsTask() {
        return new MessageRecvInitializeTask(request, response, handlerMap);
    }
}
