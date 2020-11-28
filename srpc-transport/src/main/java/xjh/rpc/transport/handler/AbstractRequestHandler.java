package xjh.rpc.transport.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xjh.rpc.transport.common.Constant;
import xjh.rpc.transport.protocol.Request;

/**
 * @author XJH
 * @date 2020/11/27
 */
@ChannelHandler.Sharable
public abstract class AbstractRequestHandler extends SimpleChannelInboundHandler<Request> {
    /**
     * 处理客户端请求，由调用方去实现
     *
     * @param ctx
     * @param request
     */
    public abstract void process(ChannelHandlerContext ctx, Request request);

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        Constant.GROUP.add(ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Request request) throws Exception {
        process(ctx, request);
    }
}
