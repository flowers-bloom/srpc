package xjh.rpc.core.provider;

import lombok.extern.slf4j.Slf4j;
import xjh.rpc.core.registry.ServiceManager;
import xjh.rpc.serialization.Serialization;
import xjh.rpc.serialization.impl.fastjson.FastJsonImpl;
import xjh.rpc.transport.common.Endpoint;
import xjh.rpc.transport.server.NettyServer;

/**
 * @Author XJH
 * @Date 2020/11/06
 */
@Slf4j
public class Provider {
    private Endpoint endpoint;
    /**
     * 当前 provider 的权重
     */
    private int weight;
    private String serviceName;
    private String registry;
    private Serialization serialization;

    public Provider serviceName(String serviceName) {
        this.serviceName = serviceName;
        return self();
    }

    public Provider address(Endpoint endpoint) {
        this.endpoint = endpoint;
        return self();
    }

    public Provider weight(int weight) {
        this.weight = weight;
        return self();
    }

    public Provider registry(String registryAddress) {
        this.registry = registryAddress;
        return self();
    }

    public Provider serialization(Serialization serialization) {
        this.serialization = serialization;
        return self();
    }

    private void export(String registryAddress, String info) {
        new ServiceManager(registryAddress)
                .register(this.serviceName, info);
    }

    private Provider self() {
        return this;
    }

    /**
     * 配置 provider 的最后调用方法
     */
    public void build() {
        if (endpoint == null) {
            throw new IllegalStateException("provider address is null");
        }

        if (serialization == null) {
            serialization = new FastJsonImpl();
        }

        new NettyServer(endpoint.getPort(), new RequestHandler(), serialization);

        if (registry != null && !"".equals(registry)) {
            weight = weight > 0 ? weight : 1;
            export(registry, endpoint.toString() + ":" + this.weight);
        }
    }
}
