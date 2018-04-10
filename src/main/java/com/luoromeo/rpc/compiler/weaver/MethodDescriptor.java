package com.luoromeo.rpc.compiler.weaver;

import java.lang.reflect.Method;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月04日 10:02
 * @modified By
 */
public class MethodDescriptor {
    private static final Map<Class<?>, Character> BUILDER = new ImmutableMap.Builder<Class<?>, Character>().put(Boolean.TYPE, 'Z')
            .put(Byte.TYPE, 'B').put(Short.TYPE, 'S').put(Integer.TYPE, 'I').put(Character.TYPE, 'C').put(Long.TYPE, 'J').put(Float.TYPE, 'F')
            .put(Double.TYPE, 'D').put(Void.TYPE, 'V').build();

    private final String internal;

    public MethodDescriptor(Method method) {
        final StringBuilder buf = new StringBuilder(method.getName()).append('(');
        for (Class<?> p : method.getParameterTypes()) {
            appendTo(buf, p);
        }

        buf.append(')');
        this.internal = buf.toString();
    }

    private static void appendTo(StringBuilder buf, Class<?> type) {
        if (type.isPrimitive()) {
            buf.append(BUILDER.get(type));
        } else if (type.isArray()) {
            buf.append('[');
            appendTo(buf, type.getComponentType());
        } else {
            buf.append('L').append(type.getName().replace('.', '/')).append(';');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }

        if (o.getClass() != getClass()) {
            return false;
        }

        MethodDescriptor other = (MethodDescriptor) o;
        return other.internal.equals(internal);
    }

    @Override
    public int hashCode() {
        return internal.hashCode();
    }

    @Override
    public String toString() {
        return internal;
    }
}
