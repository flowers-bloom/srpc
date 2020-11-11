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
    private RpcClient rpcClient;

    public ClientStub(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * 获取客户端存根
     *
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getStub(Class<T> clazz) {
        InvocationHandler h = (proxy, method, args) -> {
            Request request = new Request();

            long id = System.currentTimeMillis();

            request.setId(id);
            request.setInterfaceClazz(clazz);
            request.setMethodName(method.getName());
            request.setParameterTypes(method.getParameterTypes());
            request.setParameters(args);

            CompletableFuture<Response> future = new CompletableFuture<>();
            Constant.ID_FUTURE_MAP.put(id, future);

            rpcClient.send(request);

            Response response = future.get();
            Constant.ID_FUTURE_MAP.remove(id);

            return response.getObject();
        };

        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[] {clazz},
                h);
    }
}
