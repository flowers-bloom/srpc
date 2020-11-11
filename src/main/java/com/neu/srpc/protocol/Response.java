package com.neu.srpc.protocol;

import com.neu.srpc.codec.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author XJH
 * @Date 2020/11/07
 * @Description
 */
@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Response extends Packet {
    int code;
    long requestId;
    String message;
    Object object;

    @Override
    public Class<? extends Packet> getClazz() {
        return Response.class;
    }
}
