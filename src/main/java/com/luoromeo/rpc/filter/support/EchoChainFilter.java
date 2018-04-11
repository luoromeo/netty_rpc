
package com.luoromeo.rpc.filter.support;

import com.luoromeo.rpc.core.ModuleInvoker;
import com.luoromeo.rpc.filter.ChainFilter;
import com.luoromeo.rpc.model.MessageRequest;

public class EchoChainFilter implements ChainFilter {
    @Override
    public Object invoke(ModuleInvoker<?> invoker, MessageRequest request) throws Throwable {
        Object o = null;
        try {
            System.out.println("EchoChainFilter##TRACE MESSAGE-ID:" + request.getMessageId());
            o = invoker.invoke(request);
            return o;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw throwable;
        }
    }
}
