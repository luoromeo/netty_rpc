package com.luoromeo.rpc.core;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description rpc线程池封装
 * @author zhanghua.luo
 * @date 2018年03月29日 00:11
 * @modified By
 */
public class RpcThreadPool {

    // 独立出线程池主要是为了应对复杂耗I/O操作的业务，不阻塞netty的handler线程而引入
    // 当然如果业务足够简单，把处理逻辑写入netty的handler（ChannelInboundHandlerAdapter）也未尝不可
    public static Executor getExecutor(int threads, int queues) {
        String name = "RPCThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, queues == 0 ? new SynchronousQueue<>()
                : (queues < 0 ? new LinkedBlockingDeque<>() : new LinkedBlockingDeque<>(queues)), new NameThreadFactory(name, true),
                new AbortPolicyWithReport(name));
    }
}
