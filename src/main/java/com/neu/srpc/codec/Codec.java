package com.neu.srpc.codec;

import com.neu.srpc.protocol.HeartbeatRequest;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import io.netty.buffer.ByteBuf;

/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description 数据装包、拆包协议
 */
public class Codec {
    /**
     * 魔数
     * 区分数据包是否属于本应用，可实现一定的数据保密性
     */
    private static final byte MAGIC_NUM = 36;

    /**
     * 装包
     *
     * @param packet
     * @param out
     */
    public static void encode(Packet packet, ByteBuf out) {
        Serializer serializer = FastJsonImpl.serializer;
        byte[] bytes = serializer.deserialize(packet);

        out.writeByte(MAGIC_NUM);
        out.writeByte(classToByte(packet.getClazz()));
        out.writeInt(bytes.length);

        out.writeBytes(bytes);
    }

    /**
     * 拆包
     *
     * @param in
     * @return
     */
    public static Object decode(ByteBuf in) {
        in.skipBytes(1);
        Class<? extends Packet> clazz = byteToClass(in.readByte());

        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        return FastJsonImpl.serializer.serialize(bytes, clazz);
    }

    /**
     * class 映射到一个 byte 数，需要动态更新
     *
     * @param clazz
     * @return
     */
    private static byte classToByte(Class clazz) {
        if (clazz == Request.class) {
            return 1;
        }else if (clazz == Response.class) {
            return 2;
        }else if (clazz == HeartbeatRequest.class) {
            return 3;
        }else {
            throw new RuntimeException("unknown class type");
        }
    }

    /**
     * byte 数映射到 class 类，需要动态更新
     *
     * @param b
     * @return
     */
    private static Class<? extends Packet> byteToClass(byte b) {
        if (b == 1) {
            return Request.class;
        }else if (b == 2) {
            return Response.class;
        }else if (b == 3) {
            return HeartbeatRequest.class;
        }else {
            throw new RuntimeException("unknown int value");
        }
    }
}
