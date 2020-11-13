package com.neu.srpc.handler;

import com.neu.common.Constant;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.server.ServerStub;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description 方法调用请求的回调处理
 */
@Slf4j
public class InvokeRequestHandler extends SimpleChannelInboundHandler<Request> {
    private ChannelGroup group;
    private ServerStub serverStub;

    public InvokeRequestHandler(ServerStub serverStub) {
        this.group = Constant.GROUP;
        this.serverStub = serverStub;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        group.add(ctx.channel());
        log.info("{} connect successful", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        log.info("client request ...");
        serverStub.process(ctx, request);

//        TimeUnit.SECONDS.sleep(2);
//        ctx.disconnect();
    }

    /**
     * 心跳检测处理
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            SocketAddress remoteAddress = ctx.channel().remoteAddress();

//            if (state == IdleState.READER_IDLE) {
//                log.info("read_idle appear, server disconnect");
//                ctx.channel().disconnect();
//            }
        }else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
