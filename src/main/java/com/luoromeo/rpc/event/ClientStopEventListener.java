package com.luoromeo.rpc.event;

import com.luoromeo.rpc.netty.send.MessageSendExecutor;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月10日 09:23
 * @modified By
 */
public class ClientStopEventListener {

    public int lastMessage = 0;

    public void listen(ClientStopEvent event) {
        lastMessage = event.getMessage();
        MessageSendExecutor.getInstance().stop();
    }

    public int getLastMessage() {
        return lastMessage;
    }

}
