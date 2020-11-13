package com.neu.srpc.handler;

import com.neu.srpc.protocol.HeartbeatRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @Author XJH
 * @Date 2020/11/12
 * @Description
 */
@ChannelHandler.Sharable
public class HeartbeatSendHandler extends SimpleChannelInboundHandler<HeartbeatRequest> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartbeatRequest msg) throws Exception {
        ctx.channel().writeAndFlush(new HeartbeatRequest());
    }
}
