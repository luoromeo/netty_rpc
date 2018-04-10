package com.luoromeo.rpc.netty;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.commons.lang3.time.StopWatch;

import com.luoromeo.rpc.model.MessageRequest;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 16:41
 * @modified By
 */
public class MethodInvoker {

    private Object serviceBean;

    private StopWatch sw = new StopWatch();

    public Object getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(Object serviceBean) {
        this.serviceBean = serviceBean;
    }

    public Object invoke(MessageRequest request) throws Throwable {
        String methodName = request.getMethodName();
        Object[] parameters = request.getParametersVal();
        sw.reset();
        sw.start();
        Object result = MethodUtils.invokeMethod(serviceBean, methodName, parameters);
        sw.stop();
        return result;
    }

    public long getInvokeTimeSpan() {
        return sw.getTime();
    }
}
