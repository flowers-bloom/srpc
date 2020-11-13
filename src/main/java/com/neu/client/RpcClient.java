package com.neu.client;

import com.neu.cluster.LoadBalance;
import com.neu.cluster.RandomLoadBalance;
import com.neu.common.Endpoint;
import com.neu.srpc.codec.*;
import com.neu.srpc.handler.InvokeResponseHandler;
import com.neu.srpc.handler.ReconnectionHandler;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.registry.ServiceManager;
import com.neu.srpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;


/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description
 */
@Slf4j
public class RpcClient implements TransportClient {
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private ChannelFuture future;
    private ClientStub clientStub;
    private ServiceManager serviceManager;
    private RetryPolicy retryPolicy;
    private Endpoint endpoint;

    public RpcClient() {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.clientStub = new ClientStub(this);
        this.serviceManager = new ServiceManager();
        this.retryPolicy = new LimitedCountRetryPolicy(3);

        initBootStrap();
        select();
        connect();
    }

    /**
     * 初始化 bootstrap
     */
    private void initBootStrap() {
        bootstrap.group(group)
            .channel(Epoll.isAvailable() ? EpollSocketChannel.class :
                    NioSocketChannel.class)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();

                    pipeline.addLast(new ReconnectionHandler(RpcClient.this));
                    pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4));
                    pipeline.addLast(new Decoder());
                    pipeline.addLast(new InvokeResponseHandler());
    //                            pipeline.addLast(new HeartbeatSendHandler());
                    pipeline.addLast(new Encoder());
                }
            });
    }

    /**
     * 从注册中心获取服务列表，并随机选取一个端点初始化 ip、port
     */
    private void select() {
        serviceManager.discover();
        log.info("service points are {}", LoadBalance.SERVICE_LIST);

        LoadBalance loadBalance = new RandomLoadBalance();
        this.endpoint = loadBalance.select();
    }

    /**
     * 连接服务端
     */
    @Override
    public void connect() {
        /*
         sync 同步，连接完全建立成功后才返回 future;
         如果不使用 sync，直接发送数据不会成功，因为连接没有完全建立，但不会报错
         */
        future = bootstrap.connect(endpoint.getIp(), endpoint.getPort());
        log.info("remoteAddress = {}", future.channel().remoteAddress());

        future.addListener((ChannelFutureListener) f -> {
            if (f.isSuccess()) {
                log.info("client connect {} success.", endpoint);
            }else {
                f.channel().pipeline().fireChannelInactive();
                log.info("client connect {} failed. {}", endpoint, f.cause().getMessage());
            }
        });
    }


    /**
     * 发送消息
     *
     * @param request
     */
    public ChannelFuture send(Request request) {
        /*
        客户端主动发送数据前，保证建立的 channel 是可用的
         */
        if (!future.channel().isActive()) {
            try {
                future = future.sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return future.channel().writeAndFlush(request);
    }

    /**
     * 获取动态代理对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz) {
        return clientStub.getProxy(clazz);
    }

    /**
     * 返回重连策略实现对象
     *
     * @return
     */
    public RetryPolicy getRetryPolicy() {
        return this.retryPolicy;
    }

    /**
     * 关闭客户端
     */
    public void close() {
        this.group.shutdownGracefully();
    }

    /**
     * 获取远程主机信息
     *
     * @return
     */
    public Endpoint getEndpoint() {
        return endpoint;
    }
}
