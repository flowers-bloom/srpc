package xjh.rpc.api;

import xjh.rpc.common.spi.SPI;

/**
 * @author XJH
 * @date 2020/11/26
 */
@SPI("hi")
public interface HelloService {
    String sayHello(String s);
}
