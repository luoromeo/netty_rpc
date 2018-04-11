
package com.luoromeo.rpc.listener.support;

import org.apache.commons.lang3.StringUtils;

import com.luoromeo.rpc.core.ModuleProvider;
import com.luoromeo.rpc.listener.ModuleListener;
import com.luoromeo.rpc.model.MessageRequest;

public class ModuleListenerAdapter implements ModuleListener {
    @Override
    public void exported(ModuleProvider<?> provider, MessageRequest request) {
        System.out.println(StringUtils.center("[ModuleListenerAdapter##exported]", 48, "*"));
    }

    @Override
    public void unExported(ModuleProvider<?> provider, MessageRequest request) {
        System.out.println(StringUtils.center("[ModuleListenerAdapter##unExported]", 48, "*"));
    }
}
