package com.luoromeo.rpc.core;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;

/**
 * @description Rpc消息回调
 * @author zhanghua.luo
 * @date 2018年03月29日 09:45
 * @modified By
 */
public class MessageCallBack {

    private MessageRequest request;

    private MessageResponse response;

    private Lock lock = new ReentrantLock();

    private Condition finish = lock.newCondition();

    public MessageCallBack(MessageRequest request) {
        this.request = request;
    }

    public Object start() throws InterruptedException {
        try {
            lock.lock();
            // 设定一下超时时间，rpc服务器太久没有响应的话，就默认返回空
            finish.await(10 * 1000, TimeUnit.MILLISECONDS);
            if (this.response != null) {
                return this.response.getResultDesc();
            } else {
                return null;
            }
        } finally {
            lock.unlock();
        }
    }

    public void over(MessageResponse response) {
        try {
            lock.lock();
            finish.signal();
            this.response = response;
        } finally {
            lock.unlock();
        }
    }
}
