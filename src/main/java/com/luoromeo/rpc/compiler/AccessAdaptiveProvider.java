package com.luoromeo.rpc.compiler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.google.common.io.Files;
import com.luoromeo.rpc.compiler.intercept.SimpleMethodInterceptor;
import com.luoromeo.rpc.core.ReflectionUtils;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 10:29
 * @modified By
 */
public class AccessAdaptiveProvider extends AbstractAccessAdaptive implements AccessAdaptive {
    @Override
    protected Class<?> doCompile(String clsName, String javaSource) throws Throwable {
        File tempFileLocation = Files.createTempDir();
        compiler = new NativeCompiler(tempFileLocation);
        Class type = compiler.compile(clsName, javaSource);
        tempFileLocation.deleteOnExit();
        return type;
    }

    @Override
    public Object invoke(String javaSource, String method, Object[] args) {
        if (StringUtils.isEmpty(javaSource) || StringUtils.isEmpty(method)) {
            return null;
        } else {
            try {
                Class type = compile(javaSource, Thread.currentThread().getContextClassLoader());
                Object object = ReflectionUtils.newInstance(type);
                Thread.currentThread().getContextClassLoader().loadClass(type.getName());
                Object proxy = getFactory().createProxy(object, new SimpleMethodInterceptor(), type);
                return MethodUtils.invokeMethod(proxy, method, args);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
