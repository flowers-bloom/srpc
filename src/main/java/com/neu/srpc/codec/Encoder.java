package com.neu.srpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description 编码器
 */
@Slf4j
public class Encoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf out) throws Exception {
        log.info("Encoder has data...");

        ObjectUtil.checkNotNull(packet, "packet");
        Codec.encode(packet, out);
    }
}
