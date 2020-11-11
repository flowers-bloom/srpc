package com.neu.srpc.server;

import com.neu.srpc.transport.TransportServer;
import lombok.Data;

/**
 * @Author XJH
 * @Date 2020/11/08
 * @Description server 配置信息
 */
@Data
public class ServerConfig {
    private Class<? extends TransportServer> transportClass = RpcServer.class;
    private int port = 8000;
}
