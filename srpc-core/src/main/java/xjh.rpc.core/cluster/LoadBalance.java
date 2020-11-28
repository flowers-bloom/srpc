package xjh.rpc.core.cluster;

import xjh.rpc.transport.common.Endpoint;

import java.util.Collections;
import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description
 */
public interface LoadBalance {

    /**
     * 以特定算法选出一个端点 Endpoint
     *
     * @param objects
     * @return
     */
    Endpoint select(Object... objects);

    /**
     * 推送消息给订阅者
     * @param list
     */
    void push(List<String> list);
}
