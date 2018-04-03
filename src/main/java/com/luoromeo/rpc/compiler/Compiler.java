package com.luoromeo.rpc.compiler;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:43
 * @modified By
 */
public interface Compiler {

    Class<?> compile(String code, ClassLoader classLoader);
}
