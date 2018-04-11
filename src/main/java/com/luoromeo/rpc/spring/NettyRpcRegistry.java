package com.luoromeo.rpc.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.luoromeo.rpc.netty.server.MessageRecvExecutor;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

/**
 * @description <p>
 *              InitializingBean接口为bean提供了初始化方法的方式，它只包括afterPropertiesSet方法，
 *              凡是继承该接口的类，在初始化bean的时候会执行该方法
 *              </p>
 *              <p>
 *              DisposableBean 销毁bean前做的事情
 *              </p>
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
    public void destroy() {
        MessageRecvExecutor.getInstance().stop();
    }

    @Override
    public void afterPropertiesSet() {
        // 服务端执行模块
        MessageRecvExecutor ref = MessageRecvExecutor.getInstance();
        ref.setServerAddress(ipAddr);
        ref.setEchoApiPort(Integer.parseInt(echoApiPort));
        ref.setSerializeProtocol(Enum.valueOf(RpcSerializeProtocol.class, protocol));
        // nettyRpc服务端启动
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
