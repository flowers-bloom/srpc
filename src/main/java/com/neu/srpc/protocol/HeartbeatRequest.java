package com.neu.srpc.protocol;

import com.neu.srpc.codec.Packet;

/**
 * @Author XJH
 * @Date 2020/11/12
 * @Description 心跳请求
 */
public class HeartbeatRequest extends Packet {
    @Override
    public Class<? extends Packet> getClazz() {
        return HeartbeatRequest.class;
    }
}
