package com.neu.srpc.server;

import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author XJH
 * @Date 2020/11/10
 * @Description
 */
@Slf4j
public class ServerStub {
    private RpcServer server;

    public ServerStub(RpcServer server) {
        this.server = server;
    }


    @SuppressWarnings("unchecked")
    public void process(ChannelHandlerContext ctx, Request request) {
        Class clazz = server.discoverService(request.getInterfaceClazz());

        if (clazz == null) {
            log.error("{} service has not register", request.getInterfaceClazz());
        }

        try {
            Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(clazz.newInstance(), request.getParameters());

            ctx.channel().writeAndFlush(new Response(0, request.getId(), "", result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
