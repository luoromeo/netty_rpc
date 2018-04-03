package com.luoromeo.rpc.compiler.invoker;

import java.lang.reflect.Method;

import com.luoromeo.rpc.core.ReflectionUtils;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 15:21
 * @modified By
 */
public abstract class AbstractInvoker implements ObjectInvoker {
    @Override
    public Object invoke(Object proxy, Method method, Object... args) throws Throwable {

        if (ReflectionUtils.isHashCodeMethod(method)) {
            return System.identityHashCode(proxy);
        }

        if (ReflectionUtils.isEqualsMethod(method)) {
            return proxy == args[0];
        }
        return invokeImpl(proxy, method, args);
    }

    public abstract Object invokeImpl(Object proxy, Method method, Object[] args) throws Throwable;
}
