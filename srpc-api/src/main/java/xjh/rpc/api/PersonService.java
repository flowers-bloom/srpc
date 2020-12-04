package xjh.rpc.api;

import xjh.rpc.common.spi.SPI;

/**
 * @author XJH
 * @date 2020/12/04
 */
@SPI("man")
public interface PersonService {
    void say();
}
