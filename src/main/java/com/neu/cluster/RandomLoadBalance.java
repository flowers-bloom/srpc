package com.neu.cluster;

import com.neu.common.Endpoint;

import java.util.Random;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description 随机负载均衡
 */
public class RandomLoadBalance extends LoadBalance {
    private static Random random = new Random();

    @Override
    public Endpoint select() {
        if (SERVICE_LIST != null && SERVICE_LIST.size() > 0) {
            int index = random.nextInt(SERVICE_LIST.size());
            return SERVICE_LIST.get(index);
        }

        throw new IllegalStateException("没有可用的端点");
    }
}
