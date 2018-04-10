package com.luoromeo.rpc.async;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月10日 09:57
 * @modified By
 */
public class AsyncProxyCache {
    private static Map<String, Class> cache = Maps.newConcurrentMap();

    public static Class get(String key) {
        return cache.get(key);
    }

    public static void save(String key, Class proxyClass) {
        if (!cache.containsKey(key)) {
            synchronized (cache) {
                if (!cache.containsKey(key)) {
                    cache.put(key, proxyClass);
                }
            }
        }
    }
}
