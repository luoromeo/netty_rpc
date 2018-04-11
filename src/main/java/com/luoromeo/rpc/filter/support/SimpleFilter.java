package com.luoromeo.rpc.filter.support;

import java.lang.reflect.Method;

import org.apache.commons.lang3.StringUtils;

import com.luoromeo.rpc.filter.Filter;

public class SimpleFilter implements Filter {
    @Override
    public boolean before(Method method, Object processor, Object[] requestObjects) {
        System.out.println(StringUtils.center("[SimpleFilter##before]", 48, "*"));
        return true;
    }

    @Override
    public void after(Method method, Object processor, Object[] requestObjects) {
        System.out.println(StringUtils.center("[SimpleFilter##after]", 48, "*"));
    }
}
