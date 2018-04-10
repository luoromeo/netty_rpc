package com.luoromeo.rpc.async;

import net.sf.cglib.proxy.CallbackFilter;

import java.lang.reflect.Method;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月10日 09:51
 * @modified By
 */
public class AsyncCallFilter implements CallbackFilter {
    @Override
    public int accept(Method method) {
        return AsyncCallObject.class.isAssignableFrom(method.getDeclaringClass()) ? 1 : 0;
    }
}
