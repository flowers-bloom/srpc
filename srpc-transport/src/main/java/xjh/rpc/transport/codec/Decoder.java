package xjh.rpc.transport.codec;

import xjh.rpc.transport.protocol.Request;
import xjh.rpc.transport.protocol.Response;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.serialization.Serialization;

import java.util.List;


/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description 解码器
 */
@Slf4j
public class Decoder extends ByteToMessageDecoder {
    private Serialization serialization;

    public Decoder(Serialization serialization) {
        this.serialization = serialization;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("{} receive data length = {} byte", ctx.name(), byteBuf.readableBytes());
        }

        if (byteBuf.readableBytes() > 0) {
            list.add(decode(byteBuf));
        }
    }

    /**
     * 拆包
     *
     * @param in
     * @return
     */
    private Object decode(ByteBuf in) {
        in.skipBytes(1);
        Class<? extends Packet> clazz = byteToClass(in.readByte());

        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        return serialization.serialize(bytes, clazz);
    }

    /**
     * byte 数映射到 class 类，需要动态更新
     *
     * @param b
     * @return
     */
    private Class<? extends Packet> byteToClass(byte b) {
        if (b == 1) {
            return Request.class;
        }else if (b == 2) {
            return Response.class;
        }else {
            throw new RuntimeException("unknown int value");
        }
    }
}
