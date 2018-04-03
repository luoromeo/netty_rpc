package com.luoromeo.rpc.compiler.intercept;

import org.apache.commons.lang3.StringUtils;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 16:19
 * @modified By
 */
public class SimpleMethodInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println(StringUtils.center("[SimpleMethodInterceptor##intercept]", 48, "*"));
        // FIXME: 2017/8/30 by tangjie
        // 对于RPC客户端过来的反射请求，不能无限制地在服务端直接运行，这样可能有安全隐患！
        // 这里可以对一些敏感的关键字进行拦截处理
        // 这里可以加入你自己的业务逻辑代码。
        // TODO: your intercept logic here

        return invocation.proceed();
    }
}
