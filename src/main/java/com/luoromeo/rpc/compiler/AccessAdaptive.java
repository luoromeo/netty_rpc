package com.luoromeo.rpc.compiler;

/**
 * @description 自适应接入
 * @author zhanghua.luo
 * @date 2018年04月03日 14:44
 * @modified By
 */
public interface AccessAdaptive {
    Object invoke(String javaSource, String method, Object[] args);
}
