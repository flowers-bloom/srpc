package xjh.rpc.core.cluster;

import xjh.rpc.transport.common.Endpoint;

import java.util.List;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description
 */
public abstract class LoadBalance {
    public static List<Endpoint> SERVICE_LIST;

    /**
     * 以特定算法选出一个端点 Endpoint
     *
     * @return
     */
    public abstract Endpoint select();
}
