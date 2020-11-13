package com.neu.srpc.handler;

import com.neu.common.Constant;
import com.neu.srpc.protocol.Response;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description 方法调用后返回值的回调处理
 */
@Slf4j
public class InvokeResponseHandler extends SimpleChannelInboundHandler<Response> {

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Response resp) throws Exception {
        CompletableFuture<Response> future = Constant.ID_FUTURE_MAP.get(resp.getRequestId());
        future.complete(resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
