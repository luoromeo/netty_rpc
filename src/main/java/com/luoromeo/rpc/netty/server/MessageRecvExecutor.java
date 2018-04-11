package com.luoromeo.rpc.netty.server;

import java.lang.management.ManagementFactory;
import java.nio.channels.spi.SelectorProvider;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.luoromeo.rpc.compiler.AccessAdaptiveProvider;
import com.luoromeo.rpc.compiler.parallel.NamedThreadFactory;
import com.luoromeo.rpc.compiler.parallel.RpcThreadPool;
import com.luoromeo.rpc.core.AbilityDetailProvider;
import com.luoromeo.rpc.core.RpcSystemConfig;
import com.luoromeo.rpc.model.MessageKeyVal;
import com.luoromeo.rpc.model.MessageRequest;
import com.luoromeo.rpc.model.MessageResponse;
import com.luoromeo.rpc.netty.resolver.ApiEchoResolver;
import com.luoromeo.rpc.serialize.support.RpcSerializeProtocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
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
public class MessageRecvExecutor implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(MessageRecvExecutor.class);

    /**
     * 服务器地址 ip:port
     */
    private String serverAddress;

    /**
     * api html port
     */
    private int echoApiPort;

    /**
     * 序列化协议，默认为jdk
     */
    private RpcSerializeProtocol serializeProtocol = RpcSerializeProtocol.JDKSERIALIZE;

    /**
     * 冒号
     */
    private static final String DELIMITER = RpcSystemConfig.DELIMITER;

    /**
     * 并发数
     */
    private static final int PARALLEL = RpcSystemConfig.SYSTEM_PROPERTY_PARALLEL * 2;

    /**
     * 线程数
     */
    private static int threadNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_THREAD_NUMS;

    /**
     * 队列大小
     */
    private static int queueNums = RpcSystemConfig.SYSTEM_PROPERTY_THREADPOOL_QUEUE_NUMS;

    /**
     * <p>
     * 对应JDK中的 ExecutorService.submit(Callable) 提交多线程异步运算的方式，Guava
     * 提供了ListeningExecutorService 接口, 该接口返回 ListenableFuture，
     * 而相应的ExecutorService 返回普通的 Future。将 ExecutorService 转为
     * ListeningExecutorService
     * ，可以使用MoreExecutors.listeningDecorator(ExecutorService)进行装饰
     * </p>
     * <p>
     * ListenableFuture接口并继承了JDK concurrent包下的Future 接口，ListenableFuture
     * 允许你注册回调方法(callbacks)，在运算（多线程执行）完成的时候进行调用, 或者在运算（多线程执行）完成后立即执行
     * </p>
     */
    private static volatile ListeningExecutorService threadPoolExecutor;

    /**
     * 存放rpc的spring bean
     */
    private Map<String, Object> handlerMap = new ConcurrentHashMap<>();

    /**
     * echo 线程池大小
     */
    private int numberOfEchoThreadsPool = 1;

    /**
     * netty worker 线程工厂
     */
    private ThreadFactory threadRpcFactory = new NamedThreadFactory("NettyRPC ThreadFactory");

    /**
     * 主从Reactor多线程模型
     */
    private EventLoopGroup boss = new NioEventLoopGroup();

    private EventLoopGroup worker = new NioEventLoopGroup(PARALLEL, threadRpcFactory, SelectorProvider.provider());

    public MessageRecvExecutor() {
        handlerMap.clear();
        register();
    }

    private static class MessageRecvExecutorHolder {
        static final MessageRecvExecutor INSTANCE = new MessageRecvExecutor();
    }

    public static MessageRecvExecutor getInstance() {
        return MessageRecvExecutorHolder.INSTANCE;
    }

    /**
     * @description
     * @author zhanghua.luo
     * @date 2018年04月10日 05:03:51
     * @param task AbstractMessageRecvInitializeTask
     * @param ctx
     * @param request
     * @param response
     * @return
     */
    public static void submit(Callable<Boolean> task, final ChannelHandlerContext ctx, final MessageRequest request, final MessageResponse response) {
        if (threadPoolExecutor == null) {
            synchronized (MessageRecvExecutor.class) {
                if (threadPoolExecutor == null) {
                    threadPoolExecutor = MoreExecutors.listeningDecorator((ThreadPoolExecutor) (Objects.requireNonNull(RpcSystemConfig
                            .isMonitorServerSupport() ? RpcThreadPool.getExecutorWithJmx(threadNums, queueNums) : RpcThreadPool.getExecutor(
                            threadNums, queueNums))));
                }
            }
        }

        ListenableFuture<Boolean> listenableFuture = threadPoolExecutor.submit(task);
        Futures.addCallback(listenableFuture, new FutureCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                ctx.writeAndFlush(response).addListener(
                        (ChannelFutureListener) channelFuture -> System.out.println("RPC Server Send message-id respone:" + request.getMessageId()));
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
            }
        }, threadPoolExecutor);
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
                handlerMap.put(entry.getKey(), entry.getValue());
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MessageRecvExecutor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @description 服务端启动
     * @author zhanghua.luo
     * @date 2018年04月10日 02:16:11
     * @param
     * @return
     */
    public void start() {
        try {
            logger.info("start rpc server...");
            ServerBootstrap bootstrap = new ServerBootstrap();
            // handler在初始化时就会执行，而childHandler会在客户端成功connect后才执行
            // BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，
            // 用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50
            // 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
            bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                    .childHandler(new MessageRecvChannelInitializer(handlerMap).buildRpcSerializeProtocol(serializeProtocol))
                    .option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] ipAddr = serverAddress.split(MessageRecvExecutor.DELIMITER);

            if (ipAddr.length == RpcSystemConfig.IPADDR_OPRT_ARRAY_LENGTH) {
                final String host = ipAddr[0];
                final int port = Integer.parseInt(ipAddr[1]);
                ChannelFuture future;
                future = bootstrap.bind(host, port).sync();

                future.addListener((ChannelFutureListener) channelFuture -> {
                    if (channelFuture.isSuccess()) {
                        final ExecutorService executor = Executors.newFixedThreadPool(numberOfEchoThreadsPool);
                        // 启动
                        ExecutorCompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);
                        completionService.submit(new ApiEchoResolver(host, echoApiPort));
                        logger.info(
                                "[author luoromeo] Netty RPC Server start success!\nip:{}\nport:{}\nprotocol:{}\nstart-time:{}\njmx-invoke-metrics:{}\n\n",
                                host, port, serializeProtocol.getProtocol(), getStartTime(),
                                (RpcSystemConfig.SYSTEM_PROPERTY_JMX_METRICS_SUPPORT ? "open" : "close"));
                        // channelFuture.channel().closeFuture().sync().addListener((ChannelFutureListener)
                        // future1 -> executor.shutdownNow());
                        // sync方法会导致死锁
                        channelFuture.channel().closeFuture().addListener((ChannelFutureListener) future1 -> executor.shutdownNow());
                    }
                });
            } else {
                System.out.print("[author luoromeo] Netty RPC Server start fail!\n");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        worker.shutdownGracefully();
        boss.shutdownGracefully();
    }

    private void register() {
        handlerMap.put(RpcSystemConfig.RPC_COMPILER_SPI_ATTR, new AccessAdaptiveProvider());
        handlerMap.put(RpcSystemConfig.RPC_ABILITY_DETAIL_SPI_ATTR, new AbilityDetailProvider());
    }

    private String getStartTime() {
        String startTime;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = format.format(new Date(ManagementFactory.getRuntimeMXBean().getStartTime()));
        return startTime;
    }

    public Map<String, Object> getHandlerMap() {
        return handlerMap;
    }

    public void setHandlerMap(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RpcSerializeProtocol getSerializeProtocol() {
        return serializeProtocol;
    }

    public void setSerializeProtocol(RpcSerializeProtocol serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    public int getEchoApiPort() {
        return echoApiPort;
    }

    public void setEchoApiPort(int echoApiPort) {
        this.echoApiPort = echoApiPort;
    }
}
