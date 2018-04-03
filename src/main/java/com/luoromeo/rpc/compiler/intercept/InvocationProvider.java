package com.luoromeo.rpc.compiler.intercept;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang3.ArrayUtils;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 16:13
 * @modified By
 */
public class InvocationProvider implements Invocation {

    private final Object target;

    private final Object proxy;

    private final Method method;

    private final Object[] arguments;



    public InvocationProvider(final Object target, final Object proxy, final Method method, final Object[] arguments) {
        Object[] objects = ArrayUtils.clone(arguments);
        this.method = method;
        this.arguments = objects == null ? new Object[0] : objects;
        this.proxy = proxy;
        this.target = target;
    }

    @Override
    public Object[] getArguments() {
        return arguments;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public Object getProxy() {
        return proxy;
    }

    @Override
    public Object proceed() throws Throwable {
        try {
            return method.invoke(target, arguments);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }
}
