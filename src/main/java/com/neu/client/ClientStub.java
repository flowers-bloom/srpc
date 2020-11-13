package com.neu.client;

import com.neu.common.Constant;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.concurrent.CompletableFuture;

/**
 * @Author XJH
 * @Date 2020/11/10
 * @Description 客户端存根
 *
 */
@Slf4j
public class ClientStub {
    private RpcClient rpcClient;

    public ClientStub(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * 获取动态代理对象，并实现远程调用方法的内部逻辑
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        InvocationHandler h = (proxy, method, args) -> {
            Request request = new Request();

            long id = System.currentTimeMillis();

            request.setId(id);
            request.setInterfaceClazz(clazz);
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);

            CompletableFuture<Response> completableFuture = new CompletableFuture<>();
            Constant.ID_FUTURE_MAP.put(id, completableFuture);

            ChannelFuture future = rpcClient.send(request);
            future.addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    log.info("client send {} success.", request);
                }else {
                    log.info("client send {} failed.", request);
                }
            });

            Response response = completableFuture.get();
            Constant.ID_FUTURE_MAP.remove(id);

            return response.getObject();
        };

        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[] {clazz},
                h);
    }
}
