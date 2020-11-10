package com.neu.srpc.protocol;

import com.neu.srpc.codec.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response extends Packet {
    int code;
    long requestId;
    String message;
    Object object;

    @Override
    protected Class<? extends Packet> getClazz() {
        return Response.class;
    }
}
