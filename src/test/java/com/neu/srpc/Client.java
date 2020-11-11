package com.neu.srpc;

import com.neu.client.ClientStub;
import com.neu.client.RpcClient;
import com.neu.common.Compute;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        RpcClient client = new RpcClient();

        Compute ICompute = client.getStub(Compute.class);

        System.out.println(ICompute.add(1,2));
        System.out.println(ICompute.minus(1,2));
    }
}
