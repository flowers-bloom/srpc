package com.neu.srpc.codec;

/**
 * @Author XJH
 */
public interface Serializer {
    <T> T serialize(byte[] bytes, Class<T> clazz);

    byte[] deserialize(Object object);
}
