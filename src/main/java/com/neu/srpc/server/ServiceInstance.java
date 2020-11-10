package com.neu.srpc.server;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
@Data
@AllArgsConstructor
public class ServiceInstance {
    /**
     * 调用的对象实例
     */
    private Object object;

    /**
     * 对象实例的方法
     */
    private Method method;
}
