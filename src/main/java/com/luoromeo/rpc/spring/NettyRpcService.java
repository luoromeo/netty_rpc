package com.luoromeo.rpc.spring;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import com.luoromeo.rpc.event.ServerStartEvent;
import com.luoromeo.rpc.filter.Filter;
import com.luoromeo.rpc.filter.ServiceFilterBinder;
import com.luoromeo.rpc.netty.server.MessageRecvExecutor;

/**
 * @description NettyRpcService功能模块
 *              <P>
 *              在一些业务场景中，当容器初始化完成之后，需要处理一些操作，比如一些数据的加载、初始化缓存、特定任务的注册等等。
 *              这个时候我们就可以使用Spring提供的ApplicationListener来进行操作。
 *              </P>
 * @author zhanghua.luo
 * @date 2018年04月03日 13:57
 * @modified By
 */
public class NettyRpcService implements ApplicationContextAware, ApplicationListener {

    private String interfaceName;

    private String ref;

    private String filter;

    private ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        ServiceFilterBinder binder = new ServiceFilterBinder();
        if (StringUtils.isBlank(filter) || !(applicationContext.getBean(filter) instanceof Filter)) {
            binder.setObject(applicationContext.getBean(ref));
        } else {
            binder.setObject(applicationContext.getBean(ref));
            binder.setFilter((Filter) applicationContext.getBean(filter));
        }
        MessageRecvExecutor.getInstance().getHandlerMap().put(interfaceName, binder);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        applicationContext.publishEvent(new ServerStartEvent(new Object()));
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
