package com.luoromeo.rpc.compiler.invoker;

import java.lang.reflect.Method;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 15:20
 * @modified By
 */
public interface ObjectInvoker {

    Object invoke(Object proxy, Method method, Object... args) throws Throwable;
}
