package com.neu.srpc.server;

import com.neu.common.Constant;
import com.neu.srpc.codec.Decoder;
import com.neu.srpc.codec.Encoder;
import com.neu.srpc.handler.InvokeRequestHandler;
import com.neu.srpc.registry.ServiceManager;
import com.neu.srpc.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description
 */
@Slf4j
public class RpcServer implements TransportServer {
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture future;
    private ServerStub serverStub;
    public ServiceManager serviceManager;
    private int port;

    public RpcServer(int port) {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        this.bootstrap = new ServerBootstrap();
        this.serverStub = new ServerStub(this);
        this.serviceManager = new ServiceManager();
        this.port = port;

        bind();
    }

    /**
     * 向服务注册表注册服务
     *
     * @param interfaceClass
     * @param serviceClass
     */
    public void serviceRegister(Class interfaceClass, Class serviceClass) {
        ObjectUtil.checkNotNull(interfaceClass, "interfaceClass");
        ObjectUtil.checkNotNull(serviceClass, "interfaceClass");

        Constant.SERVICE_MAP.put(interfaceClass, serviceClass);
    }

    /**
     * 查询服务注册表，获取接口的实现类
     *
     * @param interfaceClass
     * @return
     */
    public Class discoverService(Class interfaceClass) {
        ObjectUtil.checkNotNull(interfaceClass, "interfaceClass");

        return Constant.SERVICE_MAP.get(interfaceClass);
    }

    /**
     * 监听客户端连接
     */
    @Override
    public void bind() {
        try {
            bootstrap.group(bossGroup, workerGroup)
                    .channel(Epoll.isAvailable() ?
                            EpollServerSocketChannel.class :
                            NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();

                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 2, 4));
                            pipeline.addLast(new Decoder());
//                            pipeline.addLast(new IdleStateHandler(
//                                    Constant.READ_IDLE_TIME,
//                                    0,
//                                    0,
//                                    TimeUnit.SECONDS
//                            ));
                            pipeline.addLast(new InvokeRequestHandler(serverStub));
                            pipeline.addLast(new Encoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            future = bootstrap.bind(port).sync();
            log.info("start server thread, bind {}", port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
