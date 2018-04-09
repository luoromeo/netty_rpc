package com.luoromeo.rpc.compiler.weaver;

import com.luoromeo.rpc.compiler.intercept.Interceptor;
import com.luoromeo.rpc.compiler.invoker.InterceptorInvoker;
import com.luoromeo.rpc.compiler.invoker.ObjectInvoker;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:55
 * @modified By
 */
public class ProxyProvider extends AbstractProxyProvider {
    private static final ClassCache PROXY_CLASS_CACHE = new ClassCache(new ByteCodeClassTransformer());

    @Override
    public <T> T createProxy(ClassLoader classLoader, Object target, Interceptor interceptor, Class<?>... proxyClasses) {
        return createProxy(classLoader, new InterceptorInvoker(target, interceptor), proxyClasses);
    }

    @SuppressWarnings("unchecked")
    private <T> T createProxy(ClassLoader classLoader, ObjectInvoker invoker, final Class<?>... proxyClasses) {
        Class<?> proxyClass = PROXY_CLASS_CACHE.getProxyClass(classLoader, proxyClasses);
        try {
            return (T) proxyClass.getConstructor(ObjectInvoker.class).newInstance(invoker);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
