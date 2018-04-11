
package com.luoromeo.rpc.filter;

import com.luoromeo.rpc.core.ModuleInvoker;
import com.luoromeo.rpc.model.MessageRequest;

public interface ChainFilter {
    Object invoke(ModuleInvoker<?> invoker, MessageRequest request) throws Throwable;
}
