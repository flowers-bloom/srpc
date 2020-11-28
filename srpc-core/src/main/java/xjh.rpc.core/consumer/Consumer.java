package xjh.rpc.core.consumer;

import lombok.extern.slf4j.Slf4j;
import xjh.rpc.core.cluster.LoadBalance;
import xjh.rpc.core.cluster.RandomLoadBalance;
import xjh.rpc.core.cluster.WeightedRandomLoadBalance;
import xjh.rpc.core.common.Constant;
import xjh.rpc.core.common.SyncFuture;
import xjh.rpc.core.registry.ServiceManager;
import xjh.rpc.serialization.Serialization;
import xjh.rpc.serialization.impl.fastjson.FastJsonImpl;
import xjh.rpc.transport.client.LimitedCountRetryPolicy;
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
    private Endpoint remoteAddress;
    private String name;
    private Serialization serialization;
    private RetryPolicy retryPolicy;
    private String registry;
    private ServiceManager serviceManager;
    private LoadBalance loadBalance;

    public Consumer self() {
        return this;
    }

    public Consumer remoteAddress(Endpoint remoteAddress) {
        this.remoteAddress = remoteAddress;
        return self();
    }

    public Consumer name(String name) {
        this.name = name;
        return self();
    }

    public Consumer serialization(Serialization serialization) {
        this.serialization = serialization;
        return self();
    }

    public Consumer retryPolicy(RetryPolicy retryPolicy) {
        this.retryPolicy = retryPolicy;
        return self();
    }

    public Consumer registry(String registry) {
        this.registry = registry;
        return self();
    }

    public Consumer loadBalance(LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        return self();
    }

    public void build() {
        /*
        配置注册中心，以及负载均衡方式
         */
        if (registry != null && !"".equals(registry)) {
            if (loadBalance == null) {
                loadBalance = new RandomLoadBalance();
            }

            this.serviceManager = new ServiceManager(registry, loadBalance);

            /*
            获取服务提供者
             */
            this.remoteAddress = select();
        }

        /*
        配置 Netty Client
         */
        if (remoteAddress == null) {
            throw new IllegalStateException("consumer address is null");
        }

        if (serialization == null) {
            serialization = new FastJsonImpl();
        }

        if (retryPolicy == null) {
            retryPolicy = new LimitedCountRetryPolicy(5);
        }

        client = new NettyClient(remoteAddress, new ResponseHandler(), serialization, retryPolicy);
    }

    /**
     * 从注册中心获取服务列表，并随机选取一个端点初始化 ip、port
     */
    private Endpoint select() {
        if (name == null) {
            name = "test";
        }

        serviceManager.discover();
        return loadBalance.select(name);
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
