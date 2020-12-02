package xjh.rpc.transport.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import xjh.rpc.transport.codec.Packet;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description 方法调用请求
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Request<T> extends Packet {
    private long id;
    private Class<T> interfaceClazz;
    private String methodName;
    private Class[] parameterTypes;
    private Object[] parameters;

    public Class<T> getInterfaceClazz() {
        return interfaceClazz;
    }

    @Override
    public Class<? extends Packet> getClazz() {
        return Request.class;
    }
}
