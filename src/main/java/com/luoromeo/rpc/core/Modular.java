package com.luoromeo.rpc.core;

import com.luoromeo.rpc.model.MessageRequest;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 16:18
 * @modified By
 */
public interface Modular {
    <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request);
}
