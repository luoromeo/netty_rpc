package com.luoromeo.rpc.async;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.CallbackFilter;

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
