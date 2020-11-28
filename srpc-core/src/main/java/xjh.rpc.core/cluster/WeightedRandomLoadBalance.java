package xjh.rpc.core.cluster;

import xjh.rpc.core.util.RandomUtil;
import xjh.rpc.transport.common.Endpoint;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author XJH
 * @date 2020/11/28
 *
 * 加权随机负载均衡
 */
public class WeightedRandomLoadBalance implements LoadBalance {
    private static Map<Endpoint, Integer> serviceMap;

    /**
     * 总权值
     */
    private static int sum = 0;

    @Override
    public Endpoint select(Object... objects) {
        int ans = 0;

        for (Map.Entry<Endpoint, Integer> entry : serviceMap.entrySet()) {
            ans += entry.getValue();

            int val = 1 + RandomUtil.nextInt(sum);
            if (val <= ans) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("当前没有可用服务节点");
    }

    @Override
    public void push(List<String> list) {
        sum = 0;
        Map<Endpoint, Integer> map = new ConcurrentHashMap<>(list.size());

        for (String s : list) {
            String[] tmps = s.split(":");
            Endpoint endpoint = new Endpoint(tmps[0], Integer.parseInt(tmps[1]));

            int val = Integer.parseInt(tmps[2]);
            sum += val;

            map.put(endpoint, val);
        }

        serviceMap = map;
    }
}
