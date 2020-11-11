package com.neu.client;

import com.neu.cluster.LoadBalance;
import com.neu.cluster.RandomLoadBalance;
import com.neu.common.Endpoint;
import com.neu.srpc.codec.*;
import com.neu.srpc.handler.InvokeResponseHandler;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.registry.ServiceManager;
import com.neu.srpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
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
    private String ip;
    private int port;

    public RpcClient() {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.clientStub = new ClientStub(this);
        this.serviceManager = new ServiceManager();

        init();
        connect();
    }

    /**
     * 从注册中心获取服务列表，并随机选取一个端点初始化 ip、port
     */
    private void init() {
        serviceManager.discover();

        log.info("service table is {}", LoadBalance.SERVICE_LIST);

        LoadBalance loadBalance = new RandomLoadBalance();
        Endpoint point = loadBalance.select();

        this.ip = point.getIp();
        this.port = point.getPort();

        log.info("client select point is {}:{}", ip, port);
    }

    /**
     * 连接服务端方法
     */
    @Override
    public void connect() {
        try {
            bootstrap.group(group)
                    .channel(Epoll.isAvailable() ? EpollSocketChannel.class :
                            NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();

                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4));
                            pipeline.addLast(new Decoder());
                            pipeline.addLast(new InvokeResponseHandler(RpcClient.this));
                            pipeline.addLast(new Encoder());
                        }
                    });

            future = bootstrap.connect(ip, port).sync();
            log.info("start client thread, connected at {}:{}", ip, port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 发送消息
     *
     * @param request
     */
    public void send(Request request) {
        future.channel().writeAndFlush(request);
    }

    /**
     * 获取客户端存根
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getStub(Class<T> clazz) {
        return clientStub.getStub(clazz);
    }
}
