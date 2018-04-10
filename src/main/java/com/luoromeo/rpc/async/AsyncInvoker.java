package com.luoromeo.rpc.async;

import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.luoromeo.rpc.compiler.parallel.RpcThreadPool;
import com.luoromeo.rpc.core.ReflectionUtils;
import com.luoromeo.rpc.core.RpcSystemConfig;
import com.luoromeo.rpc.exception.AsyncCallException;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月10日 09:56
 * @modified By
 */
public class AsyncInvoker {
    private ThreadPoolExecutor executor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS,
            RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS);

    public <R> R submit(final AsyncCallback<R> callback) {
        Type type = callback.getClass().getGenericInterfaces()[0];
        if (type instanceof ParameterizedType) {
            Class returnClass = (Class) ReflectionUtils.getGenericClass((ParameterizedType) type, 0);
            return intercept(callback, returnClass);
        } else {
            throw new AsyncCallException("NettyRPC AsyncCallback must be parameterized type!");
        }
    }

    private <T> AsyncFuture submit(Callable<T> task) {
        AsyncFuture future = new AsyncFuture<T>(task);
        executor.submit(future);
        return future;
    }

    private <R> R intercept(final AsyncCallback<R> callback, Class<?> returnClass) {
        if (!Modifier.isPublic(returnClass.getModifiers())) {
            return callback.call();
        } else if (Modifier.isFinal(returnClass.getModifiers())) {
            return callback.call();
        } else if (Void.TYPE.isAssignableFrom(returnClass)) {
            return callback.call();
        } else if (returnClass.isPrimitive() || returnClass.isArray()) {
            return callback.call();
        } else if (returnClass == Object.class) {
            return callback.call();
        } else {
            return submit(callback, returnClass);
        }
    }

    private <R> R submit(final AsyncCallback<R> callback, Class<?> returnClass) {
        Future future = submit(new Callable() {
            @Override
            public R call() throws Exception {
                return callback.call();
            }
        });

        AsyncCallResult result = new AsyncCallResult(returnClass, future, RpcSystemConfig.SYSTEM_PROPERTY_ASYNC_MESSAGE_CALLBACK_TIMEOUT);
        R asyncProxy = (R) result.getResult();

        return asyncProxy;
    }
}
