package xjh.rpc.core.cluster;

import xjh.rpc.core.util.RandomUtil;
import xjh.rpc.transport.common.Endpoint;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description 随机负载均衡
 */
public class RandomLoadBalance implements LoadBalance {
    private static List<Endpoint> serviceList;

    @Override
    public Endpoint select(Object... objects) {
        if (serviceList != null && serviceList.size() > 0) {
            int index = RandomUtil.nextInt(serviceList.size());
            return serviceList.get(index);
        }

        throw new IllegalStateException("没有可用的端点");
    }

    @Override
    public void push(List<String> list) {
        List<Endpoint> tmp = new ArrayList<>(list.size());

        synchronized (this) {
            for (String s : list) {
                tmp.add(new Endpoint(s));
            }

            serviceList = tmp;
        }
    }
}
