package xjh.rpc.transport.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import xjh.rpc.transport.protocol.Response;

/**
 * @author XJH
 * @date 2020/11/27
 */
@ChannelHandler.Sharable
public abstract class AbstractResponseHandler extends SimpleChannelInboundHandler<Response> {
    /**
     * 处理服务端响应，由调用方实现
     *
     * @param ctx
     * @param resp
     */
    public abstract void process(ChannelHandlerContext ctx, Response resp);

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Response resp) throws Exception {
        process(ctx, resp);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
