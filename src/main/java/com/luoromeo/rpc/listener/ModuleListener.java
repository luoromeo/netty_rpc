
package com.luoromeo.rpc.listener;

import com.luoromeo.rpc.core.ModuleProvider;
import com.luoromeo.rpc.model.MessageRequest;

public interface ModuleListener {
    void exported(ModuleProvider<?> provider, MessageRequest request);

    void unExported(ModuleProvider<?> provider, MessageRequest request);
}
