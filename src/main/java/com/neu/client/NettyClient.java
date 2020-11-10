package com.neu.client;

import com.neu.srpc.codec.*;
import com.neu.srpc.handler.ClientResponseHandler;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import com.neu.srpc.transport.TransportClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
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
public class NettyClient implements TransportClient {
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private ChannelFuture future;
    private String ip;
    private int port;

    public NettyClient(String ip, int port) {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();
        this.ip = ip;
        this.port = port;
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
                            pipeline.addLast(new ClientResponseHandler(NettyClient.this));
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
}
