package com.neu.srpc;

import com.neu.client.ClientStub;
import com.neu.common.Compute;
import com.neu.srpc.protocol.Msg;
import com.neu.client.NettyClient;
import com.neu.srpc.protocol.Request;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
public class Client {

    public static void main(String[] args) throws InterruptedException {
        Compute ICompute = new ClientStub().getStub(Compute.class);

        System.out.println(ICompute.add(1,2));
        System.out.println(ICompute.minus(1,2));
    }
}
