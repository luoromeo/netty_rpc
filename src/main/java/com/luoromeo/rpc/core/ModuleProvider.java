package com.luoromeo.rpc.core;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 16:20
 * @modified By
 */
public interface ModuleProvider<T> {
    ModuleInvoker<T> getInvoker();

    void destoryInvoker();
}
