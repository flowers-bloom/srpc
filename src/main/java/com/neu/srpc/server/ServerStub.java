package com.neu.srpc.server;

import com.neu.common.SimpleCompute;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import io.netty.channel.ChannelHandlerContext;

import java.lang.reflect.Method;

/**
 * @Author XJH
 * @Date 2020/11/10
 * @Description
 */
public class ServerStub {
    public void process(ChannelHandlerContext ctx, Request request) {


        // TODO: 寻找接口的实现类
        Class clazz = SimpleCompute.class;

        try {
            Method method = clazz.getMethod(request.getMethodName(), request.getParameterTypes());
            Object result = method.invoke(clazz.newInstance(), request.getParameters());

            ctx.channel().writeAndFlush(new Response(0, request.getId(), "", result));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
