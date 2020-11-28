package xjh.rpc.core.consumer;

import lombok.extern.slf4j.Slf4j;
import xjh.rpc.core.cluster.LoadBalance;
import xjh.rpc.core.cluster.RandomLoadBalance;
import xjh.rpc.core.common.Constant;
import xjh.rpc.core.common.SyncFuture;
import xjh.rpc.core.registry.ServiceManager;
import xjh.rpc.serialization.Serialization;
import xjh.rpc.transport.client.NettyClient;
import xjh.rpc.transport.client.RetryPolicy;
import xjh.rpc.transport.common.Endpoint;
import xjh.rpc.transport.protocol.Request;
import xjh.rpc.transport.protocol.Response;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description
 */
@Slf4j
public class Consumer {
    private NettyClient client;
    private ServiceManager serviceManager;

    /**
     * 注册中心方式
     *
     * @param registryAddress
     */
    public Consumer(String registryAddress) {
        this.serviceManager = new ServiceManager(registryAddress);
        client = new NettyClient(select(), new ResponseHandler());
    }

    /**
     * 直连方式
     *
     * @param endpoint
     */
    public Consumer(Endpoint endpoint) {
        client = new NettyClient(endpoint, new ResponseHandler());
    }

    public Consumer(Endpoint endpoint, Serialization serialization) {
        client = new NettyClient(endpoint, new ResponseHandler(), serialization);
    }

    public Consumer(Endpoint endpoint, Serialization serialization, RetryPolicy retryPolicy) {
        client = new NettyClient(endpoint, new ResponseHandler(), serialization, retryPolicy);
    }

    /**
     * 从注册中心获取服务列表，并随机选取一个端点初始化 ip、port
     */
    private Endpoint select() {
        serviceManager.discover();
        log.info("service points are {}", LoadBalance.SERVICE_LIST);

        LoadBalance loadBalance = new RandomLoadBalance();
        return loadBalance.select();
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

            SyncFuture<Response> syncFuture = new SyncFuture<>();
            Constant.ID_FUTURE_MAP.put(id, syncFuture);

            client.send(request);

            Response response = syncFuture.get();
            Constant.ID_FUTURE_MAP.remove(id);

            return response.getObject();
        };

        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(),
                new Class[] {clazz},
                h);
    }
}
