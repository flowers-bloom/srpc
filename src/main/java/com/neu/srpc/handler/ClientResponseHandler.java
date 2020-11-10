package com.neu.srpc.handler;

import com.neu.client.NettyClient;
import com.neu.common.Constant;
import com.neu.srpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description
 */
@Slf4j
public class ClientResponseHandler extends SimpleChannelInboundHandler<Response> {
    private NettyClient client;

    public ClientResponseHandler(NettyClient client) {
        this.client = client;
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channel {} has registered", ctx.channel().id());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Response resp) throws Exception {
        CompletableFuture<Response> future = Constant.map.get(resp.getRequestId());
        future.complete(resp);
    }
}
