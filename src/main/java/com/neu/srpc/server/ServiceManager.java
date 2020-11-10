package com.neu.srpc.server;

import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.ServiceInfo;
import com.neu.srpc.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
@Slf4j
public class ServiceManager {
    private static Map<ServiceInfo, ServiceInstance> map;

    public ServiceManager() {
        map = new HashMap<>();
    }

    /**
     * 服务注册
     *
     * @param interfaceClass
     * @param object
     * @param <T>
     */
    public static <T> void register(Class<T> interfaceClass, T object) {
        List<Method> methods = ReflectUtil.getPublicMethod(interfaceClass);

        for (Method method : methods) {
            ServiceInfo sif = new ServiceInfo(interfaceClass, method);
            ServiceInstance sis = new ServiceInstance(object, method);

            map.put(sif, sis);
        }
    }

    /**
     * 服务发现：返回给客户端 目标对象实例
     *
     * @param request
     * @return
     */
    public static ServiceInstance discover(Request request) {
//        ServiceInstance instance = map.get(request.getServiceInfo());
//
//        if (instance == null) {
//            log.error("unknown service {}", request.getServiceInfo());
//        }

        return null;
    }
}
