package com.luoromeo.rpc.compiler.weaver;

import com.luoromeo.rpc.compiler.intercept.Interceptor;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:55
 * @modified By
 */
public class ProxyProvider extends AbstractProxyProvider {
    @Override
    public <T> T createProxy(ClassLoader classLoader, Object target, Interceptor interceptor, Class<?>... proxyClasses) {
        return null;
    }
}
