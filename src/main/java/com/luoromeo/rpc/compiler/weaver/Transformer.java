package com.luoromeo.rpc.compiler.weaver;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:56
 * @modified By
 */
public interface Transformer {

    Class<?> transform(ClassLoader classLoader, Class<?>... proxyClasses);
}
