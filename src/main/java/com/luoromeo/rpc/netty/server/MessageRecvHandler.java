package com.luoromeo.rpc.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;
import java.util.concurrent.Callable;

import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;
import com.luoromeo.rpc.netty.RecvInitializeTaskFacade;

/**
 * @description Rpc服务器消息处理
 * @author zhanghua.luo
 * @date 2018年03月29日 14:48
 * @modified By
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> handlerMap;

    public MessageRecvHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
        RecvInitializeTaskFacade facade = new RecvInitializeTaskFacade(request, response, handlerMap);
        Callable<Boolean> recvTask = facade.getTask();
        MessageRecvExecutor.submit(recvTask, ctx, request, response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
