package com.neu.srpc.codec;

import com.neu.srpc.protocol.Msg;
import com.neu.srpc.protocol.Request;
import com.neu.srpc.protocol.Response;
import io.netty.buffer.ByteBuf;

/**
 * @Author XJH
 * @Date 2020/11/02
 * @Description
 */
public class Codec {
    private static final byte MAGIC_NUM = 36;

    public static void encode(Packet packet, ByteBuf out) {
        Serializer serializer = FastJsonImpl.serializer;
        byte[] bytes = serializer.deserialize(packet);

        out.writeByte(MAGIC_NUM);
        out.writeByte(classToByte(packet.getClazz()));
        out.writeInt(bytes.length);

        out.writeBytes(bytes);
    }

    public static Object decode(ByteBuf in) {
        in.skipBytes(1);
        Class<? extends Packet> clazz = byteToClass(in.readByte());

        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);

        return FastJsonImpl.serializer.serialize(bytes, clazz);
    }

    private static byte classToByte(Class clazz) {
        if (clazz == Request.class) {
            return 1;
        }else if (clazz == Response.class) {
            return 2;
        }else if (clazz == Msg.class) {
            return 3;
        }else {
            throw new RuntimeException("unknown class type");
        }
    }

    private static Class<? extends Packet> byteToClass(byte b) {
        if (b == 1) {
            return Request.class;
        }else if (b == 2) {
            return Response.class;
        }else if (b == 3) {
            return Msg.class;
        }else {
            throw new RuntimeException("unknown int value");
        }
    }
}
