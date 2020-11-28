package xjh.rpc.transport.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.internal.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import xjh.rpc.serialization.Serialization;
import xjh.rpc.transport.common.Constant;
import xjh.rpc.transport.protocol.Request;
import xjh.rpc.transport.protocol.Response;

/**
 * @Author XJH
 * @Date 2020/11/06
 * @Description 编码器
 */
@Slf4j
public class Encoder extends MessageToByteEncoder<Packet> {
    private Serialization serialization;

    public Encoder(Serialization serialization) {
        this.serialization = serialization;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet packet, ByteBuf out) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("{} receive data = {}", ctx.name(), packet);
        }

        ObjectUtil.checkNotNull(packet, "packet");
        encode(packet, out);
    }

    /**
     * 装包
     *
     * @param packet
     * @param out
     */
    public void encode(Packet packet, ByteBuf out) {
        byte[] bytes = serialization.deserialize(packet);

        out.writeByte(Constant.MAGIC_NUM);
        out.writeByte(classToByte(packet.getClazz()));
        out.writeInt(bytes.length);

        out.writeBytes(bytes);
    }



    /**
     * class 映射到一个 byte 数，需要动态更新
     *
     * @param clazz
     * @return
     */
    private byte classToByte(Class clazz) {
        if (clazz == Request.class) {
            return 1;
        }else if (clazz == Response.class) {
            return 2;
        }else {
            throw new RuntimeException("unknown class type");
        }
    }
}
