package com.neu.common;

import com.neu.srpc.protocol.Response;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @Author XJH
 * @Date 2020/11/09
 * @Description
 */
public class Constant {

    /**
     * 全局线程池
     */
    public static final ThreadPoolExecutor executor =
            new ThreadPoolExecutor(
                    10,
                    50,
                    3,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<>(100),
                    r -> new Thread(r, "xjh-pool-%d"),
                    new ThreadPoolExecutor.AbortPolicy());
    /**
     * netty 读空闲超时时间，单位秒。超时后，就会发送一个心跳检测包检测是否连接
     */
    public static final int READ_IDLE_TIME = 3;

    /**
     * netty 写空闲超时时间，单位秒。超时后，就会发送一个心跳检测包检测是否连接
     */
    public static final int WRITE_IDLE_TIME = 5;

    /**
     * netty 读写空闲超时时间，单位秒。超时后，就会发送一个心跳检测包检测是否连接
     */
    public static final int ALL_IDLE_TIME = 7;

    /**
     * 存储在服务端建立的 Channel
     *
     * 该容器内部实现了 serverChannels 和 noServerChannels 两个 ConcurrentHashMap，
     * 用于分别存储服务端和客户端的 Channel
     */
    public static final ChannelGroup GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 存储（RequestId，CompletableFuture）元组
     * 用于实现线程间通信
     */
    public static final Map<Long, CompletableFuture<Response>> ID_FUTURE_MAP = new ConcurrentHashMap<>();

    /**
     * 存储（接口类，实现类）元组
     * 用于获取接口的实现类
     */
    public static final Map<Class, Class> SERVICE_MAP = new ConcurrentHashMap<>();

    /**
     * zk 实例化参数
     */
    public static final String ZK_ROOT_SERVICE = "/srpc";
    public static final String ZK_ADDR = "127.0.0.1:2181";
    public static final int ZK_SESSION_TIMEOUT = 5000;

    /**
     * 测试服务名称，用于 zk 注册
     */
    public static final String TEST_SERVICE_NAME = "compute";
}
