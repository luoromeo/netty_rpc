package com.luoromeo.rpc.compiler;

import java.util.regex.Pattern;

/**
 * @description 默认自适应类
 * @author zhanghua.luo
 * @date 2018年04月03日 14:46
 * @modified By
 */
public class AbstractAccessAdaptive implements Compiler {

    private static final Pattern PACKAGE_PATTERN = Pattern.compile("package\\s+([$_a-zA-Z][$_a-zA-Z0-9.]*);");

    private static final Pattern CLASS_PATTERN = Pattern.compile("class\\s+([$_a-zA-Z][$_a-zA-Z0-9]*)\\s+");

    private static final String CLASS_END_FLAG = "}";




    @Override
    public Class<?> compile(String code, ClassLoader classLoader) {
        return null;
    }
}
