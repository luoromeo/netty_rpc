package com.luoromeo.rpc.compiler.intercept;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:48
 * @modified By
 */
public interface Interceptor {

    Object intercept(Invocation invocation) throws Throwable;
}
