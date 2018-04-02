package com.luoromeo.rpc.core.recv;

import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.luoromeo.rpc.core.NameThreadFactory;
import com.luoromeo.rpc.core.RpcThreadPool;
import com.luoromeo.rpc.model.MessageKeyVal;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @description Rpc服务端执行模块
 * @author zhanghua.luo
 * @date 2018年03月29日 14:09
 * @modified By
 */
public class MessageRecvExecutor implements ApplicationContextAware, InitializingBean {

    private String serverAddress;

    private final static String DELIMITER = ":";

    private Map<String, Object> handleMap = new ConcurrentHashMap<>();

    private static ThreadPoolExecutor threadPoolExecutor;

    public MessageRecvExecutor(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public static void submit(Runnable task) {
        if (threadPoolExecutor == null) {
            synchronized (MessageRecvExecutor.class) {
                threadPoolExecutor = (ThreadPoolExecutor) RpcThreadPool.getExecutor(16, -1);
            }
        }
        threadPoolExecutor.submit(task);
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        try {
            MessageKeyVal keyVal = (MessageKeyVal) ctx.getBean(Class.forName("com.luoromeo.rpc.model.MessageKeyVal"));
            Map<String, Object> rpcServiceObject = keyVal.getMessageKeyVal();

            Set<Map.Entry<String, Object>> s = rpcServiceObject.entrySet();
            Iterator<Map.Entry<String, Object>> it = s.iterator();
            Map.Entry<String, Object> entry;

            while (it.hasNext()) {
                entry = it.next();
                handleMap.put(entry.getKey(), entry.getValue());
            }

        } catch (ClassNotFoundException e) {
            java.util.logging.Logger.getLogger(MessageRecvExecutor.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        //netty的线程池模型设置成主从模式，这样可以应对高并发要求
        //当然netty还支持单线程、多线程网络IO模型，可以根据业务需求灵活配置
        ThreadFactory threadFactory = new NameThreadFactory("NettyRPC ThreadFactory");

        //方法返回到Java虚拟机的可用的处理器数量
        int parallel = Runtime.getRuntime().availableProcessors() * 2;

        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup(parallel, threadFactory, SelectorProvider.provider());

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new MessageRecvChannelInitializer(handleMap))
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            String[] ipAddr = serverAddress.split(MessageRecvExecutor.DELIMITER);

            if (ipAddr.length == 2) {
                String host = ipAddr[0];
                int port = Integer.valueOf(ipAddr[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();
                System.out.printf("[author tangjie] Netty RPC Server start success ip:%s port:%d\n", host, port);
                future.channel().closeFuture().sync();
            } else {
                System.out.printf("[author tangjie] Netty RPC Server start fail!\n");
            }
        } finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
