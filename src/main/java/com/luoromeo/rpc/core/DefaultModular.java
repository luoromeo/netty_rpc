package com.luoromeo.rpc.core;

import com.luoromeo.rpc.model.MessageRequest;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 16:21
 * @modified By
 */
public class DefaultModular implements Modular {

    @Override
    public <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request) {
        return new ModuleProvider<T>() {
            @Override
            public ModuleInvoker<T> getInvoker() {
                return invoker;
            }

            @Override
            public void destoryInvoker() {
                invoker.destroy();
            }
        };
    }
}
