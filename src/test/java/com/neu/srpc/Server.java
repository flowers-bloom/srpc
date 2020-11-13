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
        int port = 9000;
        String serviceName = "test";
        String ip = "127.0.0.1";

        RpcServer server = new RpcServer(port);

        // 注册接口及实现类
        server.serviceRegister(Compute.class, SimpleCompute.class);

        // 注册服务
        server.serviceManager.register(serviceName, ip, port);
    }
}
