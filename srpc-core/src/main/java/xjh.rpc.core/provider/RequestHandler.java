package xjh.rpc.core.provider;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.transport.handler.AbstractRequestHandler;
import xjh.rpc.transport.protocol.Request;
import xjh.rpc.transport.protocol.Response;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.ServiceLoader;

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
        ServiceLoader serviceLoader = ServiceLoader.load(request.getInterfaceClazz());
        Iterator iterator = serviceLoader.iterator();
        if (!iterator.hasNext()) {
            log.error("接口 {} 实现类未声明", request.getInterfaceClazz());
            throw new RuntimeException("iterator not has next");
        }

        // 接口实现类对象
        Object instance = iterator.next();
        ObjectUtil.checkNotNull(instance, "instance");

        try {
            Method method = instance.getClass().getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(instance, request.getParameters());

            ctx.channel().writeAndFlush(new Response(0, request.getId(), "", result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
