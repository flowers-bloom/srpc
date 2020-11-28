package xjh.rpc.transport.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.serialization.Serialization;
import xjh.rpc.serialization.impl.fastjson.FastJsonImpl;
import xjh.rpc.transport.codec.Decoder;
import xjh.rpc.transport.codec.Encoder;
import xjh.rpc.transport.codec.LengthFieldDecoder;
import xjh.rpc.transport.common.Endpoint;
import xjh.rpc.transport.handler.AbstractResponseHandler;
import xjh.rpc.transport.handler.ReconnectionHandler;
import xjh.rpc.transport.protocol.Request;

/**
 * @author XJH
 * @date 2020/11/27
 */
@Slf4j
public class NettyClient {
    private Endpoint endpoint;
    private Serialization serialization;
    private RetryPolicy retryPolicy;
    private AbstractResponseHandler responseHandler;
    private NioEventLoopGroup group;
    private Bootstrap bootstrap;
    private ChannelFuture future;

    public NettyClient(Endpoint endpoint, AbstractResponseHandler responseHandler) {
        this(endpoint, responseHandler, new FastJsonImpl());
    }

    public NettyClient(Endpoint endpoint, AbstractResponseHandler responseHandler, Serialization serialization) {
        this(endpoint, responseHandler, serialization, new LimitedCountRetryPolicy(5));
    }

    public NettyClient(Endpoint endpoint, AbstractResponseHandler responseHandler, Serialization serialization, RetryPolicy retryPolicy) {
        this.endpoint = endpoint;
        this.serialization = serialization;
        this.retryPolicy = retryPolicy;
        this.responseHandler = responseHandler;

        init();
        connect();
    }

    /**
     * 初始化 bootstrap
     */
    private void init() {
        this.group = new NioEventLoopGroup();
        this.bootstrap = new Bootstrap();

        bootstrap.group(group)
                .channel(Epoll.isAvailable() ? EpollSocketChannel.class :
                        NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();

                        pipeline.addLast("Consumer ReconnectionHandler", new ReconnectionHandler(NettyClient.this));
                        pipeline.addLast("Consumer LengthFieldDecoder", new LengthFieldDecoder());
                        pipeline.addLast("Consumer Decoder", new Decoder(serialization));
                        pipeline.addLast("Consumer ResponseHandler", responseHandler);
                        pipeline.addLast("Consumer Encoder", new Encoder(serialization));
                    }
                });
    }

    /**
     * 连接服务端
     */
    public void connect() {
        if (endpoint == null) {
            throw new RuntimeException("endpoint is null");
        }

        /*
         sync 同步，连接完全建立成功后才返回 future;
         如果不使用 sync，直接发送数据不会成功，因为连接没有完全建立，但不会报错
         */
        future = bootstrap.connect(endpoint.getIp(), endpoint.getPort());

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

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public RetryPolicy getRetryPolicy() {
        return retryPolicy;
    }

    /**
     * 关闭客户端
     */
    public void close() {
        this.group.shutdownGracefully();
    }
}
