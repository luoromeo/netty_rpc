package com.luoromeo.rpc.compiler.invoker;

import java.lang.reflect.Method;

import com.luoromeo.rpc.compiler.intercept.Interceptor;
import com.luoromeo.rpc.compiler.intercept.InvocationProvider;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 16:12
 * @modified By
 */
public class InterceptorInvoker extends AbstractInvoker {

    private final Object target;

    private final Interceptor methodInterceptor;

    public InterceptorInvoker(Object target, Interceptor methodInterceptor) {
        this.target = target;
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public Object invokeImpl(Object proxy, Method method, Object[] args) throws Throwable {
        InvocationProvider invocation = new InvocationProvider(target, proxy, method, args);
        return methodInterceptor.intercept(invocation);
    }
}
