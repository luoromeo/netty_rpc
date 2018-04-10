package com.luoromeo.rpc.spring;

import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import com.google.common.eventbus.EventBus;
import com.luoromeo.rpc.event.ClientStopEvent;
import com.luoromeo.rpc.event.ClientStopEventListener;
import com.luoromeo.rpc.netty.send.MessageSendExecutor;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 15:16
 * @modified By
 */
public class NettyRpcReference implements FactoryBean, InitializingBean, DisposableBean {

    @Getter
    @Setter
    private String interfaceName;

    @Getter
    @Setter
    private String ipAddr;

    @Getter
    @Setter
    private String protocol;

    private EventBus eventBus = new EventBus();

    @Override
    public void destroy() throws Exception {
        eventBus.post(new ClientStopEvent(0));
    }

    @Override
    public Object getObject() throws Exception {
        return MessageSendExecutor.getInstance().execute(getObjectType());
    }

    @Override
    public Class<?> getObjectType() {
        try {
            return this.getClass().getClassLoader().loadClass(interfaceName);
        } catch (ClassNotFoundException e) {
            System.err.println("spring analyze fail!");
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageSendExecutor.getInstance().setRpcServerLoader(ipAddr, RpcSerializeProtocol.valueOf(protocol));
        ClientStopEventListener listener = new ClientStopEventListener();
        eventBus.register(listener);
    }
}
