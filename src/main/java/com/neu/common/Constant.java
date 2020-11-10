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
//    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
//            .setNameFormat("demo-pool-%d").build();
//    ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,
//            0L, TimeUnit.MILLISECONDS,
//            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
//
//    singleThreadPool.execute(()-> System.out.println(Thread.currentThread().getName()));
//    singleThreadPool.shutdown();

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

    public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static final Map<Long, CompletableFuture<Response>> map = new ConcurrentHashMap<>();
}
