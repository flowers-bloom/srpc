package com.neu.srpc.protocol;

import com.neu.srpc.codec.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description 方法调用请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Request extends Packet {
    private long id;
    private Class interfaceClazz;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameters;

    @Override
    public Class<? extends Packet> getClazz() {
        return Request.class;
    }
}
