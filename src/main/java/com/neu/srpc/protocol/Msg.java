package com.neu.srpc.protocol;

import com.neu.srpc.codec.Packet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author XJH
 * @Date 2020/11/09
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg extends Packet {
    int a;

    @Override
    protected Class<? extends Packet> getClazz() {
        return Msg.class;
    }
}
