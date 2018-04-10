package com.luoromeo.rpc.compiler.parallel;

import java.util.Objects;
import java.util.Timer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.luoromeo.rpc.compiler.parallel.policy.AbortPolicy;
import com.luoromeo.rpc.compiler.parallel.policy.BlockingPolicy;
import com.luoromeo.rpc.compiler.parallel.policy.CallerRunsPolicy;
import com.luoromeo.rpc.compiler.parallel.policy.DiscardedPolicy;
import com.luoromeo.rpc.compiler.parallel.policy.RejectedPolicy;
import com.luoromeo.rpc.compiler.parallel.policy.RejectedPolicyType;
import com.luoromeo.rpc.core.RpcSystemConfig;

/**
 * @description rpc线程池封装
 * @author zhanghua.luo
 * @date 2018年03月29日 00:11
 * @modified By
 */
public class RpcThreadPool {
    private static Timer TIMER = new Timer("ThreadPoolMonitor", true);

    private static long monitorDelay = 100L;

    private static long monitorPeriod = 300L;

    private static RejectedExecutionHandler createPolicy() {
        RejectedPolicyType rejectedPolicyType = RejectedPolicyType.fromString(System.getProperty(
                RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_REJECTED_POLICY_ATTR, "AbortPolicy"));

        switch (rejectedPolicyType) {
            case BLOCKING_POLICY:
                return new BlockingPolicy();
            case CALLER_RUNS_POLICY:
                return new CallerRunsPolicy();
            case ABORT_POLICY:
                return new AbortPolicy();
            case REJECTED_POLICY:
                return new RejectedPolicy();
            case DISCARDED_POLICY:
                return new DiscardedPolicy();
            default: {
                break;
            }
        }

        return null;
    }

    private static BlockingQueue<Runnable> createBlockingQueue(int queues) {
        BlockingQueueType queueType = BlockingQueueType.fromString(System.getProperty(RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NAME_ATTR,
                "LinkedBlockingQueue"));

        switch (queueType) {
            case LINKED_BLOCKING_QUEUE:
                return new LinkedBlockingQueue<Runnable>();
            case ARRAY_BLOCKING_QUEUE:
                return new ArrayBlockingQueue<Runnable>(RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * queues);
            case SYNCHRONOUS_QUEUE:
                return new SynchronousQueue<Runnable>();
            default: {
                break;
            }
        }

        return null;
    }

    public static Executor getExecutor(int threads, int queues) {
        System.out.println("ThreadPool Core[threads:" + threads + ", queues:" + queues + "]");
        String name = "RpcThreadPool";
        return new ThreadPoolExecutor(threads, threads, 0, TimeUnit.MILLISECONDS, Objects.requireNonNull(createBlockingQueue(queues)),
                new NamedThreadFactory(name, true), Objects.requireNonNull(createPolicy()));
    }

    public static Executor getExecutorWithJmx(int threads, int queues) {
        // todo 监控线程池
        return null;
    }
}
