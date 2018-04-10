package com.luoromeo.rpc.netty;

import java.lang.reflect.Method;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import com.luoromeo.rpc.filter.Filter;
import com.luoromeo.rpc.filter.ServiceFilterBinder;
import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.netty.MethodInvoker;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 16:53
 * @modified By
 */
public class MethodProxyAdvisor implements MethodInterceptor {

    private Map<String, Object> handlerMap;

    private Boolean returnNotNull = true;

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    public MethodProxyAdvisor(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        Object[] params = invocation.getArguments();

        if (params.length <= 0) {
            return null;
        }

        MessageRequest request = (MessageRequest) params[0];

        String className = request.getClassName();

        Object serviceBean = handlerMap.get(className);

        String methodName = request.getMethodName();

        Object[] parameters = request.getParametersVal();

        boolean existFilter = ServiceFilterBinder.class.isAssignableFrom(serviceBean.getClass());

        ((MethodInvoker) invocation.getThis()).setServiceBean(existFilter ? ((ServiceFilterBinder) serviceBean).getObject() : serviceBean);

        if (existFilter) {
            ServiceFilterBinder processors = (ServiceFilterBinder) serviceBean;
            if (processors.getFilter() != null) {
                Filter filter = processors.getFilter();
                Object[] args = ArrayUtils.nullToEmpty(parameters);
                Class<?>[] parameterTypes = ClassUtils.toClass(args);
                Method method = MethodUtils.getMatchingAccessibleMethod(processors.getObject().getClass(), methodName, parameterTypes);
                if (filter.before(method, processors.getObject(), parameters)) {
                    Object result = invocation.proceed();
                    filter.after(method, processors.getObject(), parameters);
                    setReturnNotNull(result != null);
                    return result;
                } else {
                    return null;
                }
            }
        }

        Object result = invocation.proceed();
        setReturnNotNull(result != null);
        return result;
    }
}