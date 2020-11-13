package com.neu.srpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;


/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description 解码器
 */
@Slf4j
public class Decoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        log.info("decode receive data:{}", byteBuf.readableBytes());

        if (byteBuf.readableBytes() > 0) {
            list.add(Codec.decode(byteBuf));
        }
    }
}
