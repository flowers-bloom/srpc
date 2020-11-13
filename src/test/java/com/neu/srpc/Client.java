package com.neu.srpc;

import com.neu.client.RpcClient;
import com.neu.common.Compute;

import java.util.concurrent.TimeUnit;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        RpcClient client = new RpcClient();

        // 获取代理对象
        Compute ICompute = client.getProxy(Compute.class);

        // 远程方法调用
        System.out.println("[result]: " + ICompute.add(1,2));
//        System.out.println(ICompute.minus(1,2));
    }
}
