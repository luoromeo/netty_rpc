package com.luoromeo.rpc.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description 线程工厂
 * @author zhanghua.luo
 * @date 2018年03月28日 23:40
 * @modified By
 */
public class NameThreadFactory implements ThreadFactory {

    private static final AtomicInteger THREAD_NUM = new AtomicInteger(1);

    private final AtomicInteger mThreadNum = new AtomicInteger(1);

    private final String prefix;

    private final boolean daemonThread;

    private final ThreadGroup threadGroup;

    public NameThreadFactory() {
        this("rpcserver-threadpool-" + THREAD_NUM.getAndIncrement(), false);
    }

    public NameThreadFactory(String prefix) {
        this(prefix, false);
    }

    public NameThreadFactory(String prefix, boolean daemonThread) {
        this.prefix = prefix + "-thread-";
        this.daemonThread = daemonThread;
        SecurityManager s = System.getSecurityManager();
        threadGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        String name = prefix + mThreadNum.getAndIncrement();
        Thread ret = new Thread(threadGroup, r, name, 0);
        ret.setDaemon(daemonThread);
        return ret;
    }

    public ThreadGroup getThreadGroup() {
        return this.threadGroup;
    }
}