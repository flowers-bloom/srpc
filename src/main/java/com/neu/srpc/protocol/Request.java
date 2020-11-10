package com.neu.srpc.protocol;

import com.neu.srpc.codec.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request extends Packet {
    private long id;
    private String clazzName;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameters;

    @Override
    protected Class<? extends Packet> getClazz() {
        return Request.class;
    }
}
