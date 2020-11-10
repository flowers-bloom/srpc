package com.neu.client;

import com.neu.common.Constant;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * @Author XJH
 * @Date 2020/11/10
 * @Description 客户端存根
 *
 * TODO: 客户端选择端点进行连接
 */
@Slf4j
public class ClientStub {
    private NettyClient nettyClient;

    public ClientStub() {
        nettyClient = new NettyClient("127.0.0.1", 8000);
        nettyClient.connect();
    }

    /**
     * 获取客户端存根
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getStub(Class<T> clazz) {
        InvocationHandler h = (proxy, method, args) -> {
            Request request = new Request();

            long id = System.currentTimeMillis();

            request.setId(id);
            request.setClazzName(clazz.getName());
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);

            CompletableFuture<Response> future = new CompletableFuture<>();
            Constant.map.put(id, future);

            nettyClient.send(request);

            Response response = future.get();
            Constant.map.remove(id);

            return response.getObject();
        };

        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[] {clazz},
                h);
    }
}
