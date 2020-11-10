package com.neu.srpc;

import com.neu.srpc.server.NettyServer;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description
 */
public class Server {
    public static void main(String[] args) {
        NettyServer server = new NettyServer(8000);
        server.bind();
    }
}
