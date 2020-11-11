package com.neu.srpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.ObjectUtil;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description 编码器
 */
public class Encoder extends MessageToByteEncoder<Packet> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Packet packet, ByteBuf out) throws Exception {
        ObjectUtil.checkNotNull(packet, "packet");
        Codec.encode(packet, out);
    }
}
