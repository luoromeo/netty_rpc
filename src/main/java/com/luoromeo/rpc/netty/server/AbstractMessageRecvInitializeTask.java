package com.luoromeo.rpc.netty.server;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.Callable;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor;

import com.luoromeo.rpc.core.Modular;
import com.luoromeo.rpc.core.ModuleInvoker;
import com.luoromeo.rpc.core.ModuleProvider;
import com.luoromeo.rpc.core.RpcSystemConfig;
import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;
import com.luoromeo.rpc.netty.MethodInvoker;
import com.luoromeo.rpc.netty.MethodProxyAdvisor;
import com.luoromeo.rpc.spring.BeanFactoryUtils;

/**
 * @description 消息任务
 * @author zhanghua.luo
 * @date 2018年04月09日 16:15
 * @modified By
 */
public abstract class AbstractMessageRecvInitializeTask implements Callable<Boolean> {

    protected MessageRequest request;

    protected MessageResponse response;

    protected Map<String, Object> handleMap;

    protected static final String METHOD_MAPPED_NAME = "invoke";

    protected boolean returnNotNull = true;

    protected long invokeTimeSpan;

    protected Modular modular = BeanFactoryUtils.getBean("modular");

    public AbstractMessageRecvInitializeTask(MessageRequest request, MessageResponse response, Map<String, Object> handleMap) {
        this.request = request;
        this.response = response;
        this.handleMap = handleMap;
    }

    @Override
    public Boolean call() {
        try {
            acquire();
            response.setMessageId(request.getMessageId());
            injectInvoke();
            Object result = reflect(request);
            boolean isInvokeSuccess = (!returnNotNull || result != null);
            if (isInvokeSuccess) {
                response.setResult(result);
                response.setError("");
                response.setReturnNotNull(returnNotNull);
                injectSuccInvoke(invokeTimeSpan);
            } else {
                System.err.println(RpcSystemConfig.FILTER_RESPONSE_MSG);
                response.setResult(null);
                response.setError(RpcSystemConfig.FILTER_RESPONSE_MSG);
                injectFilterInvoke();
            }
            return Boolean.TRUE;
        } catch (Throwable t) {
            response.setError(getStackTrace(t));
            t.printStackTrace();
            System.err.print("RPC Server invoke error!\n");
            injectFailInvoke(t);
            return Boolean.FALSE;
        } finally {
            release();
        }

    }

    private Object reflect(MessageRequest request) throws Throwable {

        ProxyFactory weaver = new ProxyFactory(new MethodInvoker());

        // spring 切面
        NameMatchMethodPointcutAdvisor advisor = new NameMatchMethodPointcutAdvisor();
        advisor.setMappedName(METHOD_MAPPED_NAME);
        advisor.setAdvice(new MethodProxyAdvisor(handleMap));
        weaver.addAdvisor(advisor);

        MethodInvoker mi = (MethodInvoker) weaver.getProxy();
        Object obj = invoke(mi, request);
        invokeTimeSpan = mi.getInvokeTimeSpan();
        setReturnNotNull(((MethodProxyAdvisor) advisor.getAdvice()).isReturnNotNull());
        return obj;
    }

    @SuppressWarnings("unchecked")
    private Object invoke(MethodInvoker mi, MessageRequest request) throws Throwable {
        if (modular != null) {
            ModuleProvider provider = modular.invoke(new ModuleInvoker<Object>() {
                @Override
                public Class getInterface() {
                    return mi.getClass().getInterfaces()[0];
                }

                @Override
                public Object invoke(MessageRequest request) throws Throwable {
                    return mi.invoke(request);
                }

                @Override
                public void destroy() {

                }
            }, request);
            return provider.getInvoker().invoke(request);
        } else {
            return mi.invoke(request);
        }
    }

    public String getStackTrace(Throwable ex) {
        StringWriter buf = new StringWriter();
        ex.printStackTrace(new PrintWriter(buf));

        return buf.toString();
    }

    public boolean isReturnNotNull() {
        return returnNotNull;
    }

    public void setReturnNotNull(boolean returnNotNull) {
        this.returnNotNull = returnNotNull;
    }

    public MessageResponse getResponse() {
        return response;
    }

    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    protected abstract void injectInvoke();

    protected abstract void injectSuccInvoke(long invokeTimespan);

    protected abstract void injectFailInvoke(Throwable error);

    protected abstract void injectFilterInvoke();

    protected abstract void acquire();

    protected abstract void release();
}
