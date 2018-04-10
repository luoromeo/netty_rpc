package com.luoromeo.rpc.async;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月10日 09:54
 * @modified By
 */
public enum CallStatus {
    RUN, TIMEOUT, DONE;

    public boolean isRun() {
        return this == RUN;
    }

    public boolean isTimeout() {
        return this == TIMEOUT;
    }

    public boolean isDone() {
        return this == DONE;
    }
}
