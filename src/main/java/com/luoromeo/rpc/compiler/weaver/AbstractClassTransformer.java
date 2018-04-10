package com.luoromeo.rpc.compiler.weaver;

import java.beans.MethodDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:57
 * @modified By
 */
public class AbstractClassTransformer implements Transformer {

    @Override
    public Class<?> transform(ClassLoader classLoader, Class<?>... proxyClasses) {
        return null;
    }

    protected Method[] findImplementationMethods(Class<?>[] proxyClasses) {
        Map<MethodDescriptor, Method> descriptorMap = new HashMap<>();

        Set<MethodDescriptor> finalSet = new HashSet<>();

        for (Class<?> proxyInterface : proxyClasses) {
            Method[] methods = proxyInterface.getMethods();
            for (Method method : methods) {
                MethodDescriptor descriptor = new MethodDescriptor(method);
                if (Modifier.isFinal(method.getModifiers())) {
                    finalSet.add(descriptor);
                } else if (!descriptorMap.containsKey(descriptor)) {
                    descriptorMap.put(descriptor, method);
                }

            }
        }

        Collection<Method> results = descriptorMap.values();
        for (MethodDescriptor signature : finalSet) {
            results.remove(descriptorMap.get(signature));
        }

        return results.toArray(new Method[0]);
    }
}
