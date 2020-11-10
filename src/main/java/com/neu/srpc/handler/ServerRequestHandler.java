package com.neu.srpc.handler;

import com.neu.common.Constant;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.server.NettyServer;
import com.neu.srpc.server.ServerStub;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description
 */
@Slf4j
public class ServerRequestHandler extends SimpleChannelInboundHandler<Request> {
    private ChannelGroup group;
    private ServerStub serverStub;

    public ServerRequestHandler(ServerStub serverStub) {
        this.group = Constant.group;
        this.serverStub = serverStub;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        group.add(ctx.channel());
        log.info("{} connect successful", ctx.channel().remoteAddress());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        serverStub.process(ctx, request);
    }
}
