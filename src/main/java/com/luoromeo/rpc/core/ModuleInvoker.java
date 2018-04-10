package com.luoromeo.rpc.core;

import com.luoromeo.rpc.model.MessageRequest;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 16:19
 * @modified By
 */
public interface ModuleInvoker<T> {

    Class<T> getInterface();

    Object invoke(MessageRequest request) throws Throwable;

    void destroy();
}
