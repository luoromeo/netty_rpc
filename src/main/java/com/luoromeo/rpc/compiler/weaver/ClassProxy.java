package com.luoromeo.rpc.compiler.weaver;

import com.luoromeo.rpc.compiler.intercept.Interceptor;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:52
 * @modified By
 */
public interface ClassProxy {

    <T> T createProxy(Object target, Interceptor interceptor, Class<?>... proxyClasses);

    <T> T createProxy(ClassLoader classLoader, Object target, Interceptor interceptor, Class<?>... proxyClasses);
}
