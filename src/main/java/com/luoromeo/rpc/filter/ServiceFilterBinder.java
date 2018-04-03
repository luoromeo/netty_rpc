package com.luoromeo.rpc.filter;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月03日 14:08
 * @modified By
 */
public class ServiceFilterBinder {

    private Object object;

    private Filter filter;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
}
