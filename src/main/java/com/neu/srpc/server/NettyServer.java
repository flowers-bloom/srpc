package com.neu.srpc.server;

import com.neu.common.SimpleCompute;
import com.neu.srpc.codec.Decoder;
import com.neu.srpc.codec.Encoder;
import com.neu.srpc.handler.ServerRequestHandler;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import com.neu.srpc.transport.TransportServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description
 */
@Slf4j
public class NettyServer implements TransportServer {
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture future;
    private ServerStub serverStub;
    private int port;

    public NettyServer(int port) {
        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        this.bootstrap = new ServerBootstrap();
        this.serverStub = new ServerStub();
        this.port = port;
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
                            pipeline.addLast(new ServerRequestHandler(serverStub));
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
