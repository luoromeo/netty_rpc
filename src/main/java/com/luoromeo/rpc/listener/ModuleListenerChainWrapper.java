package com.luoromeo.rpc.listener;

import java.util.Collections;
import java.util.List;

import com.luoromeo.rpc.core.Modular;
import com.luoromeo.rpc.core.ModuleInvoker;
import com.luoromeo.rpc.core.ModuleProvider;
import com.luoromeo.rpc.model.MessageRequest;

public class ModuleListenerChainWrapper implements Modular {
    private Modular modular;

    private List<ModuleListener> listeners;

    public ModuleListenerChainWrapper(Modular modular) {
        if (modular == null) {
            throw new IllegalArgumentException("module is null");
        }
        this.modular = modular;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> ModuleProvider<T> invoke(ModuleInvoker<T> invoker, MessageRequest request) {
        return new ModuleProviderWrapper(modular.invoke(invoker, request), Collections.unmodifiableList(listeners), request);
    }

    public List<ModuleListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ModuleListener> listeners) {
        this.listeners = listeners;
    }
}
