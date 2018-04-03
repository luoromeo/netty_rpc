package com.luoromeo.rpc.filter;

import java.lang.reflect.Method;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:11
 * @modified By
 */
public interface Filter {

    boolean before(Method method, Object object, Object[] requestObjects);

    void after(Method method, Object processor, Object[] requestObjects);
}
