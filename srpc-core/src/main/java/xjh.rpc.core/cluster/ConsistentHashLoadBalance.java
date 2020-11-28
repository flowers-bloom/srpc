package xjh.rpc.core.cluster;

import xjh.rpc.transport.common.Endpoint;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author XJH
 * @date 2020/11/28
 */
public class ConsistentHashLoadBalance implements LoadBalance {
    private static final int VIRTUAL_NODES = 160;
    private static TreeMap<Integer, Endpoint> nodes = new TreeMap<>();

    @Override
    public Endpoint select(Object... objects) {
        if (objects.length != 1) {
            throw new IllegalArgumentException("参数个数异常");
        }

        String address = (String) objects[0];
        int hash = getHash(address);

        SortedMap<Integer, Endpoint> tailMap = nodes.tailMap(hash);
        /*
        获取大于 hash 的第一个元素
         */
        Integer nodeIndex = tailMap.firstKey();

        if (nodeIndex == null) {
            /*
            如果没有大于的元素，则选择环中第一个元素
             */
            nodeIndex = nodes.firstKey();
        }

        return nodes.get(nodeIndex);
    }

    private int getHash(String address) {
        final int p = 16723123;
        int hash = 231476589;

        for (int i=0; i<address.length(); i++) {
            hash = (hash ^ address.charAt(i)) * p;
        }

        hash += hash << 13;
        hash ^= hash >> 7;

        return hash;
    }

    @Override
    public void push(List<String> list) {
        for (String s : list) {
            for (int i=0; i<VIRTUAL_NODES; i++) {
                String tmp = s + "VN" + i;

                String[] split = s.split(":");
                nodes.put(getHash(tmp),
                        new Endpoint(split[0], Integer.parseInt(split[1])));
            }
        }
    }
}
