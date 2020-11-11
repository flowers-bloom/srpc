package com.neu.srpc;

import com.neu.common.Compute;
import com.neu.common.Constant;
import com.neu.common.SimpleCompute;
import com.neu.srpc.server.RpcServer;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
public class Server {
    public static void main(String[] args) {
        RpcServer server = new RpcServer(9001);

        server.serviceRegister(Compute.class, SimpleCompute.class);
        server.serviceManager.register("other",
                "127.0.0.1", 9001);
    }
}
