package com.luoromeo.rpc.compiler.weaver;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description ClassCache功能模块
 * @author zhanghua.luo
 * @date 2018年04月03日 17:15
 * @modified By
 */
public class ClassCache {

    /**
     * 引入弱引用节省内存消耗，JVM每次gc的时候，自动释放内存
     */
    private final Map<ClassLoader, Map<Set<Class<?>>, WeakReference<Class<?>>>> loader = new HashMap<>();

    private final Transformer transformer;

    public ClassCache(Transformer transformer) {
        this.transformer = transformer;
    }

    private Map<Set<Class<?>>, WeakReference<Class<?>>> getClassCache(ClassLoader classLoader) {
        return loader.computeIfAbsent(classLoader, k -> new HashMap<>(512));
    }

    private Set<Class<?>> toClassCacheKey(Class<?>[] proxyClasses) {
        return new HashSet<Class<?>>(Arrays.asList(proxyClasses));
    }

    public synchronized Class<?> getProxyClass(ClassLoader classLoader, Class<?>[] proxyClasses) {
        Map<Set<Class<?>>, WeakReference<Class<?>>> classCache = getClassCache(classLoader);
        Set<Class<?>> key = toClassCacheKey(proxyClasses);
        Class<?> proxyClass;
        Reference<Class<?>> proxyClassReference = classCache.get(key);

        if (proxyClassReference == null) {
            proxyClass = transformer.transform(classLoader, proxyClasses);
            classCache.put(key, new WeakReference<>(proxyClass));
        } else {
            proxyClass = proxyClassReference.get();
            if (proxyClass == null) {
                proxyClass = transformer.transform(classLoader, proxyClasses);
                classCache.put(key, new WeakReference<Class<?>>(proxyClass));
            }
        }
        return proxyClass;
    }
}
