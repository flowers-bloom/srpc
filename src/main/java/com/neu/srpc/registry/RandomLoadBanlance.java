package com.neu.srpc.registry;

import java.util.Random;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description 随机负载均衡
 */
public class RandomLoadBanlance extends LoadBalance {
    private static Random random = new Random();

    @Override
    public String select() {
        String hostAddr = "";

        if (SERVICE_LIST != null && SERVICE_LIST.size() > 0) {
            int index = random.nextInt(SERVICE_LIST.size());
            hostAddr = SERVICE_LIST.get(index);
        }

        return hostAddr;
    }
}
