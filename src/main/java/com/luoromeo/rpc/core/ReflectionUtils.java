package com.luoromeo.rpc.core;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableMap;
import com.luoromeo.rpc.exception.CreateProxyException;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 15:27
 * @modified By
 */
public class ReflectionUtils {

    private static ImmutableMap.Builder<Class, Object> builder = ImmutableMap.builder();

    private StringBuilder provider = new StringBuilder();

    public StringBuilder getProvider() {
        return provider;
    }

    public void clearProvider() {
        provider.delete(0, provider.length());
    }

    static {
        builder.put(Boolean.class, Boolean.FALSE);
        builder.put(Byte.class, (byte) 0);
        builder.put(Character.class, (char) 0);
        builder.put(Short.class, (short) 0);
        builder.put(Double.class, (double) 0);
        builder.put(Float.class, (float) 0);
        builder.put(Integer.class, 0);
        builder.put(Long.class, 0L);
        builder.put(boolean.class, Boolean.FALSE);
        builder.put(byte.class, (byte) 0);
        builder.put(char.class, (char) 0);
        builder.put(short.class, (short) 0);
        builder.put(double.class, (double) 0);
        builder.put(float.class, (float) 0);
        builder.put(int.class, 0);
        builder.put(long.class, 0L);

    }

    /**
     * 过滤得到interfaces
     * @param proxyClasses
     * @return
     */
    public static Class<?>[] filterInterfaces(Class<?>[] proxyClasses) {
        Set<Class<?>> interfaces = new HashSet<>();
        for (Class<?> proxyClass : proxyClasses) {
            if (proxyClass.isInterface()) {
                interfaces.add(proxyClass);
            }
        }
        interfaces.add(Serializable.class);
        return interfaces.toArray(new Class[0]);
    }

    /**
     * 过滤得到不是接口的类
     * @param proxyClasses
     * @return
     */
    public static Class<?>[] filterNonInterfaces(Class<?>[] proxyClasses) {
        Set<Class<?>> superclasses = new HashSet<>();
        for (Class<?> proxyClass : proxyClasses) {
            if (!proxyClass.isInterface()) {
                superclasses.add(proxyClass);
            }
        }

        return superclasses.toArray(new Class[0]);
    }

    /**
     * 是否存在默认构造器
     * @param superclass
     * @return
     */
    public static boolean existDefaultConstructor(Class<?> superclass) {
        final Constructor<?>[] declaredConstructors = superclass.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            boolean exist = (constructor.getParameterTypes().length == 0 && (Modifier.isPublic(constructor.getModifiers()) || Modifier
                    .isProtected(constructor.getModifiers())));
            if (exist) {
                return true;
            }
        }

        return false;
    }

    /**
     * 获取父类
     * @param proxyClasses
     * @return
     */
    public static Class<?> getParentClass(Class<?>[] proxyClasses) {
        final Class<?>[] parent = filterNonInterfaces(proxyClasses);
        switch (parent.length) {
            case 0:
                return Object.class;
            case 1:
                Class<?> superclass = parent[0];
                if (Modifier.isFinal(superclass.getModifiers())) {
                    throw new CreateProxyException("proxy can't build " + superclass.getName() + " because it is final");
                }
                if (!existDefaultConstructor(superclass)) {
                    throw new CreateProxyException("proxy can't build " + superclass.getName() + ", because it has no default constructor");
                }

                return superclass;
            default:
                StringBuilder errorMessage = new StringBuilder("proxy class can't build");
                for (int i = 0; i < parent.length; i++) {
                    Class<?> c = parent[i];
                    errorMessage.append(c.getName());
                    if (i != parent.length - 1) {
                        errorMessage.append(", ");
                    }
                }

                errorMessage.append("; multiple implement not allowed");
                throw new CreateProxyException(errorMessage.toString());
        }
    }

    public static boolean isHashCodeMethod(Method method) {
        return "hashCode".equals(method.getName()) && Integer.TYPE.equals(method.getReturnType()) && method.getParameterTypes().length == 0;
    }

    public static boolean isEqualsMethod(Method method) {
        return "equals".equals(method.getName()) && Boolean.TYPE.equals(method.getReturnType()) && method.getParameterTypes().length == 1
                && Object.class.equals(method.getParameterTypes()[0]);
    }

    @SuppressWarnings("unchecked")
    public static Object newInstance(Class type) {
        Constructor constructor = null;
        Object[] args = new Object[0];
        try {
            constructor = type.getConstructor();
        } catch (NoSuchMethodException ignore) {
        }

        if (constructor == null) {
            Constructor[] constructors = type.getConstructors();
            if (constructors.length == 0) {
                return null;
            }
            constructor = constructors[0];
            Class[] params = constructor.getParameterTypes();
            args = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                args[i] = getDefaultVal(params[i]);
            }
        }

        try {
            return constructor.newInstance(args);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getDefaultVal(Class cl) {
        if (cl.isArray()) {
            return Array.newInstance(cl.getComponentType(), 0);
        } else if (cl.isPrimitive() || builder.build().containsKey(cl)) {
            return builder.build().get(cl);
        } else {
            return newInstance(cl);
        }
    }

    public static Class<?> getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];

        if (genericClass instanceof GenericArrayType) {
            return (Class<?>) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) genericClass).getRawType();
        } else {
            return (Class<?>) genericClass;
        }
    }

    private String modifiers(int m) {
        return m != 0 ? Modifier.toString(m) + " " : "";
    }

    private String getType(Class<?> t) {
        StringBuilder brackets = new StringBuilder();
        while (t.isArray()) {
            brackets.append("[]");
            t = t.getComponentType();
        }
        return t.getName() + brackets;
    }

    private void listTypes(Class<?>[] types) {
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                provider.append(", ");
            }
            provider.append(getType(types[i]));
        }
    }

    private void listField(Field f, boolean html) {
        provider.append(html ? "&nbsp&nbsp" : "  ").append(modifiers(f.getModifiers())).append(getType(f.getType())).append(" ").append(f.getName())
                .append(html ? ";<br>" : ";\n");
    }

    public void listMethod(Executable member, boolean html) {
        provider.append(html ? "<br>&nbsp&nbsp" : "\n  " + modifiers(member.getModifiers() & (~Modifier.FINAL)));
        if (member instanceof Method) {
            provider.append(getType(((Method) member).getReturnType())).append(" ");
        }
        provider.append(member.getName()).append("(");
        listTypes(member.getParameterTypes());
        provider.append(")");
        Class<?>[] exceptions = member.getExceptionTypes();
        if (exceptions.length > 0) {
            provider.append(" throws ");
        }
        listTypes(exceptions);
        provider.append(";");
    }

    public void listRpcProviderDetail(Class<?> c, boolean html) {
        if (c.isInterface()) {
            provider.append(Modifier.toString(c.getModifiers())).append(" ").append(c.getName());
            provider.append(html ? " {<br>" : " {\n");

            boolean hasFields = false;
            Field[] fields = c.getDeclaredFields();
            if (fields.length != 0) {
                provider.append(html ? "&nbsp&nbsp//&nbspFields<br>" : "  // Fields\n");
                hasFields = true;
                for (Field field : fields) {
                    listField(field, html);
                }
            }

            provider.append(hasFields ? (html ? "<br>&nbsp&nbsp//&nbspMethods" : "\n  // Methods") : (html ? "&nbsp&nbsp//&nbspMethods"
                    : "  // Methods"));
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                listMethod(method, html);
            }
            provider.append(html ? "<br>}<p>" : "\n}\n\n");
        }
    }

    public static Method getDeclaredMethod(Class<?> cls, String methodName, Class<?>... parameterTypes) {
        Method method;
        Class<?> searchType = cls;
        while (searchType != null) {
            method = findDeclaredMethod(searchType, methodName, parameterTypes);
            if (method != null) {
                return method;
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * 获取公开的方法
     * @param cls
     * @param methodName
     * @param parameterTypes
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Method findDeclaredMethod(final Class<?> cls, final String methodName, final Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = cls.getDeclaredMethod(methodName, parameterTypes);
            return method;
        } catch (NoSuchMethodException e) {
            for (Method m : cls.getDeclaredMethods()) {
                if (m.getName().equals(methodName)) {
                    boolean find = true;
                    Class[] paramType = m.getParameterTypes();
                    if (paramType.length != parameterTypes.length) {
                        continue;
                    }
                    for (int i = 0; i < parameterTypes.length; i++) {
                        if (!paramType[i].isAssignableFrom(parameterTypes[i])) {
                            find = false;
                            break;
                        }
                    }
                    if (find) {
                        method = m;
                        break;
                    }
                }
            }
        }
        return method;
    }

    /**
     * 获得类类型
     * @param types
     * @return
     */
    private String getClassType(Class<?>[] types) {
        StringBuilder type = new StringBuilder();
        for (int i = 0; i < types.length; i++) {
            if (i > 0) {
                type.append(", ");
            }
            type.append(getType(types[i]));
        }
        return type.toString();
    }

    /**
     * 获取类签名
     * @param cls
     * @return
     */
    public List<String> getClassMethodSignature(Class<?> cls) {
        List<String> list = new ArrayList<>();
        if (cls.isInterface()) {
            Method[] methods = cls.getDeclaredMethods();
            StringBuilder signatureMethod = new StringBuilder();
            for (Method member : methods) {
                int modifiers = member.getModifiers();
                if (Modifier.isAbstract(modifiers) && Modifier.isPublic(modifiers)) {
                    signatureMethod.append(modifiers(Modifier.PUBLIC));
                    if (Modifier.isFinal(modifiers)) {
                        signatureMethod.append(modifiers(Modifier.FINAL));
                    }
                } else {
                    signatureMethod.append(modifiers);
                }

                signatureMethod.append(getType(((Method) member).getReturnType())).append(" ");
                signatureMethod.append(member.getName()).append("(");
                signatureMethod.append(getClassType(member.getParameterTypes()));
                signatureMethod.append(")");
                Class<?>[] exceptions = member.getExceptionTypes();
                if (exceptions.length > 0) {
                    signatureMethod.append(" throws ");
                }
                listTypes(exceptions);
                signatureMethod.append(";");
                list.add(signatureMethod.toString());
                signatureMethod.delete(0, signatureMethod.length());
            }
        }
        return list;
    }
}
