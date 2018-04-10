package com.luoromeo.rpc.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.luoromeo.rpc.netty.server.MessageRecvExecutor;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

/**
 * @description
 * @author zhanghua.luo
 * @date 2018年04月09日 15:03
 * @modified By
 */
public class NettyRpcRegistry implements InitializingBean, DisposableBean {

    private String ipAddr;

    private String protocol;

    private String echoApiPort;

    private AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

    @Override
    public void destroy() throws Exception {
        MessageRecvExecutor.getInstance().stop();
        ;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        MessageRecvExecutor ref = MessageRecvExecutor.getInstance();
        ref.setServerAddress(ipAddr);
        ref.setEchoApiPort(Integer.parseInt(echoApiPort));
        ref.setSerializeProtocol(Enum.valueOf(RpcSerializeProtocol.class, protocol));

        ref.start();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEchoApiPort() {
        return echoApiPort;
    }

    public void setEchoApiPort(String echoApiPort) {
        this.echoApiPort = echoApiPort;
    }
}
