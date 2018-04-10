package com.luoromeo.rpc.async;

import net.sf.cglib.proxy.LazyLoader;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月10日 09:55
 * @modified By
 */
public class AsyncCallResultInterceptor implements LazyLoader {

    private AsyncCallResult result;

    public AsyncCallResultInterceptor(AsyncCallResult result) {
        this.result = result;
    }

    @Override
    public Object loadObject() throws Exception {
        return result.loadFuture();
    }

}
