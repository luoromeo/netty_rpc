package com.luoromeo.rpc.event;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 15:26
 * @modified By
 */
public class ClientStopEvent {

    private final int message;

    public ClientStopEvent(int message) {
        this.message = message;
    }

    public int getMessage() {
        return message;
    }
}
