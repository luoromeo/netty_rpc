package com.luoromeo.rpc.compiler;

import java.io.IOException;
import java.net.URI;

import javax.tools.SimpleJavaFileObject;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 09:47
 * @modified By
 */
public class StringJavaFileObject extends SimpleJavaFileObject {

    private static final String SCHEME = "string:///";

    private final String code;

    protected StringJavaFileObject(String className, String code) {
        super(URI.create(SCHEME + className.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return code;
    }
}
