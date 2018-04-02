package com.luoromeo.rpc.core.recv;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Map;

import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;

/**
 * @description Rpc服务器消息处理
 * @author zhanghua.luo
 * @date 2018年03月29日 14:48
 * @modified By
 */
public class MessageRecvHandler extends ChannelInboundHandlerAdapter {

    private final Map<String, Object> handleMap;

    public MessageRecvHandler(Map<String, Object> handleMap) {
        this.handleMap = handleMap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request = (MessageRequest) msg;
        MessageResponse response = new MessageResponse();
        MessageRecvInitializeTask recvTask = new MessageRecvInitializeTask(request, response, handleMap, ctx);
        //不要阻塞nio线程，复杂的业务逻辑丢给专门的线程池
        MessageRecvExecutor.submit(recvTask);
    }
}
