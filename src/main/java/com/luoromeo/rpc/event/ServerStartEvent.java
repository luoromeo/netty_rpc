package com.luoromeo.rpc.event;

import org.springframework.context.ApplicationEvent;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:07
 * @modified By
 */
public class ServerStartEvent extends ApplicationEvent {
    public ServerStartEvent(Object source) {
        super(source);
    }
}
