package com.luoromeo.rpc.compiler.intercept;

import java.lang.reflect.Method;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:49
 * @modified By
 */
public interface Invocation {

    Object[] getArguments();

    Method getMethod();

    Object getProxy();

    Object proceed() throws Throwable;
}
