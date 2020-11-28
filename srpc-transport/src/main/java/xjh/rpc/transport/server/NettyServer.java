package xjh.rpc.transport.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.serialization.Serialization;
import xjh.rpc.serialization.impl.fastjson.FastJsonImpl;
import xjh.rpc.transport.codec.Decoder;
import xjh.rpc.transport.codec.Encoder;
import xjh.rpc.transport.codec.LengthFieldDecoder;
import xjh.rpc.transport.handler.AbstractRequestHandler;

/**
 * @author XJH
 * @date 2020/11/27
 */
@Slf4j
public class NettyServer {
    private int port;
    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ServerBootstrap bootstrap;
    private AbstractRequestHandler requestHandler;
    private Serialization serialization;

    public NettyServer(int port, AbstractRequestHandler requestHandler) {
        this(port, requestHandler, new FastJsonImpl());
    }

    public NettyServer(int port, AbstractRequestHandler requestHandler, Serialization serialization) {
        this.port = port;
        this.requestHandler = requestHandler;
        this.serialization = serialization;

        this.bossGroup = new NioEventLoopGroup(1);
        this.workerGroup = new NioEventLoopGroup();
        this.bootstrap = new ServerBootstrap();

        bind();
    }

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

                            pipeline.addLast("Provider LengthFieldDecoder", new LengthFieldDecoder());
                            pipeline.addLast("Provider Decoder", new Decoder(serialization));
                            pipeline.addLast("Provider RequestHandler", requestHandler);
                            pipeline.addLast("Provider Encoder", new Encoder(serialization));
                        }
                    });

            bootstrap.bind(port).sync();
            log.info("start provider thread, bind {}", port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
