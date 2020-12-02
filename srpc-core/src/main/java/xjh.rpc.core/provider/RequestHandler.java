package xjh.rpc.core.provider;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.common.spi.ExtensionLoader;
import xjh.rpc.transport.handler.AbstractRequestHandler;
import xjh.rpc.transport.protocol.Request;
import xjh.rpc.transport.protocol.Response;

import java.lang.reflect.Method;

/**
 * @Author XJH
 * @Date 2020/11/10
 * @Description 服务端存根
 */
@Slf4j
public class RequestHandler extends AbstractRequestHandler {

    /**
     * 处理客户端远程方法调用，并返回结果
     *
     * @param ctx
     * @param request
     */
    @SuppressWarnings("unchecked")
    @Override
    public void process(ChannelHandlerContext ctx, Request request) {
        ExtensionLoader loader = ExtensionLoader.load(request.getInterfaceClazz());
        Object instance = loader.getExtension();

        try {
            Method method = instance.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(instance, request.getParameters());

            ctx.channel().writeAndFlush(new Response(0, request.getId(), "", result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
