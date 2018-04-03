package com.luoromeo.rpc.compiler.weaver;

import com.luoromeo.rpc.compiler.intercept.Interceptor;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:53
 * @modified By
 */
public abstract class AbstractProxyProvider implements ClassProxy {

    @Override
    public <T> T createProxy(Object target, Interceptor interceptor, Class<?>... proxyClasses) {
        return createProxy(Thread.currentThread().getContextClassLoader(), target, interceptor, proxyClasses);
    }
}
