package xjh.rpc.core.provider;

import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.core.common.Constant;
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
    private NettyServer server;
    private Endpoint endpoint;

    public Provider(Endpoint endpoint, boolean hasRegistry, String registryAddress) {
        this(endpoint, new FastJsonImpl(), hasRegistry, registryAddress);
    }

    public Provider(Endpoint endpoint, Serialization serialization, boolean hasRegistry, String registryAddress) {
        this.endpoint = endpoint;
        server = new NettyServer(endpoint.getPort(), new RequestHandler(), serialization);

        if (hasRegistry) {
            export(registryAddress);
        }
    }

    private void export(String registryAddress) {
        new ServiceManager(registryAddress).register("test");
    }

    /**
     * 向服务注册表注册服务
     *
     * @param interfaceClass
     * @param serviceClass
     */
    public void serviceRegister(Class interfaceClass, Class serviceClass) {
        ObjectUtil.checkNotNull(interfaceClass, "interfaceClass");
        ObjectUtil.checkNotNull(serviceClass, "interfaceClass");

        Constant.SERVICE_MAP.put(interfaceClass, serviceClass);
    }

    /**
     * 查询服务注册表，获取接口的实现类
     *
     * @param interfaceClass
     * @return
     */
    public Class discoverService(Class interfaceClass) {
        ObjectUtil.checkNotNull(interfaceClass, "interfaceClass");

        return Constant.SERVICE_MAP.get(interfaceClass);
    }
}
